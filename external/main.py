from abc import abstractmethod, ABC, ABCMeta

import textx as tx
import os

ID_REGISTRY = dict()
READABLE_BRICK = dict()


class Model(object):
    def __init__(self, bricks, states, init_state, serial, frequency):
        self.bricks = bricks
        self.states = states
        self.init_state = init_state
        self.serial = serial
        self.frequency = frequency

        self.max_state_sleep = int(1000 * (1 / frequency)) if frequency > 0 else None

    def generate_loop_code(self):
        return 'void loop() {{ {init_state}(); }}'.format(init_state=self.init_state.name)

    def generate_var_init_code(self):
        return '\n'.join([e.generate_var_init_code() for e in self.bricks])

    def generate_setup_code(self):
        setup = open("templates/setup.amlt", 'r').read()

        serial_code = self.serial.generate_setup_code() if self.serial is not None else ''

        return setup.format(setup_code='\n\t' + serial_code +'\n\t'.join([e.generate_setup_code() for e in self.bricks]))

    def generate_states_code(self):
        return '\n'.join([str(e) for e in self.states])

    def generate_includes(self):
        includes = ""
        for e in self.bricks:
            for k in e.dependencies():
                includes += "#include <{}.h>\n".format(k)
        return includes

    def __str__(self):
        out = open("header.txt", 'r').read()
        out += self.generate_includes()
        out += self.generate_var_init_code()
        out += self.generate_setup_code()
        out += self.generate_states_code()
        out += self.generate_loop_code()
        return out


class Brick(object):
    def __init__(self, parent, name, pin):
        self.parent = parent
        self.pin = pin
        self.name = name

    def generate_var_init_code(self):
        return 'int {} = {};'.format(self.name, self.pin)

    def dependencies(self):
        return ""

class Sensor(Brick):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return 'pinMode({}, INPUT);'.format(self.name, self.pin)


class Actuator(Brick):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return 'pinMode({}, OUTPUT);'.format(self.name, self.pin)


class AnalogicSensor(Sensor):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def inline_read_code(self):
        return 'analogRead({})'.format(self.name)

    def __str__(self):
        return '{}'.format(self.name)



class DigitalSensor(Sensor):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def inline_read_code(self):
        return 'digitalRead({})'.format(self.name)

    def __str__(self):
        return '{}'.format(self.name)



class DigitalActuator(Actuator):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def assignment_code(self):
        return 'digitalWrite({})'.format(self.name)

    def __str__(self):
        return self.name


class AnalogicActuator(Actuator):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def __str__(self):
        return 'analogRead({})'.format(self.name)


class LiquidCrystal:
    def __init__(self, parent, name, pin, matrix_size):
        self.matrix_size = matrix_size
        self.pin = pin
        self.parent = parent
        self.name = name

    def generate_var_init_code(self):
        return 'LiquidCrystal {}({});'.format(self.name, ','.join([str(e) for e in self.pin]))

    def generate_setup_code(self):
        return '\t\n{}.begin({}, {});'.format(self.name, self.matrix_size[0], self.matrix_size[1])

    def dependencies(self):
        return ['LiquidCrystal']

class Action:
    def __init__(self, parent):
        self.parent = parent


class Wait:
    def __init__(self, parent, milli):
        self.parent = parent
        self.milli = milli

    def __str__(self):
        return "\n\tdelay({});".format(self.milli)


class State:
    def __init__(self, parent, name, statements):
        self.parent = parent
        self.statements = statements
        self.name = name
        self.model_freq = self.parent._txa_frequency
        self.max_state_sleep = int(1000 * (1 / self.model_freq)) if self.model_freq > 0 else None

        action_exprs = list(filter(lambda e: type(e) is Wait, self.statements))
        self.sleep_count_in_expr = sum([e.milli for e in action_exprs])

    def __str__(self):
        state = open('templates/state.amlt').read()
        no_transition = True
        time_to_sleep_before_next_state = self.max_state_sleep
        state_inner_code = ''
        for expr in self.statements:
            if type(expr) is Wait and self.max_state_sleep is not None:
                time_to_sleep_before_next_state -= expr.milli
            if type(expr) is Transition:
                no_transition = False
                state_inner_code += '\n\t' + expr.generate_code(time_to_sleep_before_next_state)
            else:
                state_inner_code += '\n\t' + str(expr)

        if time_to_sleep_before_next_state is not None and time_to_sleep_before_next_state < 0:
            print('[WARNING] Total wait actions {}ms exceed period {}ms in state "{}"'.format(self.sleep_count_in_expr,
                                                                                              int(
                                                                                                  1000 / self.model_freq),
                                                                                              self.name))

        if no_transition and self.max_state_sleep is not None:
            state_inner_code += "\tdelay({});".format(time_to_sleep_before_next_state)

        end_state_sleep = ''
        if time_to_sleep_before_next_state is not None and time_to_sleep_before_next_state > 0 :
            end_state_sleep = 'delay({});'.format(time_to_sleep_before_next_state)

        return state.format(name=self.name, code=state_inner_code, sleep_remain=end_state_sleep)

class Exception:
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value

    def __str__(self):
        return 'error({});'.format(self.value)

class Transition:
    def __init__(self, parent, condition, next_state, exception):
        self.parent = parent
        self.condition = condition
        self.next_state = next_state
        self.exception = exception

    def generate_code(self, delay_before_next_state):
        if delay_before_next_state is not None and delay_before_next_state > 0:
            delay_instr = 'delay({});\n\t\t'.format(delay_before_next_state)

        else:
            delay_instr = ''
        if self.exception is not None:
            next_state = self.exception
        else :
            next_state = self.next_state.name
        if type(next_state) is not Exception:
            next_state += '();'

        if self.condition is None:
            return "{delay_instr}\n\t{next_state}".format(
                delay_instr=delay_instr,
                next_state=next_state)

        transition = open('templates/transition.amlt').read()
        return transition.format(condition=self.condition,
                                 next_state=next_state,
                                 delay_instr=delay_instr)

class Print:
    def __init__(self, parent, cmd, output, from_brick, msg):
        self.parent = parent
        self.cmd = cmd
        self.output = output
        self.from_brick = from_brick
        self.msg = msg

    def __str__(self):
        # LCD mode
        cmd = 'print' if self.cmd == "PRINT" else "println"
        if type(self.output) is LiquidCrystal:
            payload = "prettyPrint(\"{}\",{})".format(self.from_brick.name,self.from_brick.inline_read_code())\
                if self.from_brick is not None else "\"" + self.msg.ljust(self.output.matrix_size[0]) + "\""

            return '{lcd_name}.flush();\n\t{lcd_name}.setCursor(0, 0);\n' \
                '\t{lcd_name}.{cmd}({payload});\n'.format(lcd_name=self.output.name,cmd=cmd, payload=payload)

        if type(self.output) is Serial:
            payload = "prettyPrint(\"{}\",{})".format(self.from_brick.name, self.from_brick.inline_read_code()) if self.from_brick is not None else "\"" + self.msg + "\""

            return 'Serial.{cmd}({payload});\n'.format(cmd=cmd, payload=payload)


class Goto:
    def __init__(self, parent, next_state):
        self.parent = parent
        self.next_state = next_state

    def __str__(self):
        return '{}();'.format(self.next_state.name)


class DigitalValue:
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value

    def __str__(self):
        return 'HIGH' if self.value == 'ON' else 'LOW'


class Assignment:
    def __init__(self, parent, var,var_analog, new_value, value):
        self.parent = parent
        self.var = var
        self.new_value = new_value
        self.value = value
        self.var_analog = var_analog

    def __str__(self):
        if type(self.var) is AnalogicActuator:
            if self.new_value is not None:
                return "analogWrite({}, {});".format(self.var.name, self.new_value.inline_read_code())
            else:
                return "analogWrite({}, {});".format(self.var.name, self.value)
        else:
            if self.new_value is not None:
                return "digitalWrite({}, {});".format(self.var.name, self.new_value.inline_read_code())
            else:
                return "digitalWrite({}, {});".format(self.var.name, self.value)


class AssignmentFromBrick:
    def __init__(self, parent, var, new_value):
        self.parent = parent
        self.var = var
        self.new_value = new_value

    def __str__(self):
        if type(self.new_value) is DigitalValue:
            return '{} = {};'.format(self.var.assignment_code(), self.new_value)
        else:
            return '{} = {};'.format(self.var.assignment_code(), self.new_value)


class Comparable:
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value

    def __str__(self):
        return '{}'.format(self.value)


class Condition:
    def __init__(self, parent, l, op, r):
        self.parent = parent
        self.l = l
        self.op = op
        self.r = r

    def __str__(self):
        if self.op is None:
            return str(self.l)

        return '{} {} {}'.format(self.l, self.op, self.r)


class ConditionTerm:
    def __init__(self, parent, l, op, r, serial):
        self.parent = parent
        self.l = l
        self.op = op
        self.r = r
        self.serial = serial

    def __str__(self):
        if self.serial:
            return '{}.indexOf("{}") >= 0'.format(self.serial.inline_read_code(), self.r)

        return '{} {} {}'.format(self.l.inline_read_code(), self.op, self.r)

class Serial:
    def __init__(self, parent, name, baudrate):
        self.parent = parent
        self.name = name
        self.baudrate = baudrate

    def generate_setup_code(self):
        return 'Serial.begin({});'.format(self.baudrate)

    def inline_read_code(self):
        return 'Serial.readString()'

type_builtins = {
    'ON': DigitalValue(None, 'ON'),
    'OFF': DigitalValue(None, 'OFF')
}

if __name__ == '__main__':

    classes = [Model,Print, ConditionTerm, Transition, Wait, Action, Assignment,
               AssignmentFromBrick,
               Condition, Comparable,
               Brick, State,Serial, DigitalValue,Exception, LiquidCrystal, DigitalSensor, AnalogicActuator,DigitalActuator, AnalogicSensor,
               Goto]

    meta_model = tx.metamodel_from_file('grammar.tx', classes=classes, builtins=type_builtins)
    try:
        os.mkdir('out')
    except FileExistsError:
        pass

    for file_name in os.listdir('samples'):
        ID_REGISTRY = dict()
        READABLE_BRICK = dict()
        print("Translating {}".format(file_name))
        model = meta_model.model_from_file('samples/{}'.format(file_name))

        out = open('out/{}'.format(file_name.replace('.aml', '.ino')), 'w')
        print(model, file=out)
        out.close()
