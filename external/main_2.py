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

    def generate_var_init_code(self):
        return '\n'.join([e.generate_var_init_code() for e in self.bricks])

    def generate_setup_code(self):
        setup = open("templates/setup.amlt", 'r').read()
        return setup.format(setup_code='\n\t' + '\n\t'.join([e.generate_setup_code() for e in self.bricks]))

    def generate_states_code(self):
        return '\n\n'.join([str(e) for e in self.states])

    def __str__(self):
        out = self.generate_var_init_code()
        out += self.generate_setup_code()
        out += self.generate_states_code()
        return out


class Brick(object):
    def __init__(self, parent, name, pin):
        self.parent = parent
        self.pin = pin
        self.name = name

    def __str__(self):
        return 'def brick {} pin={}'.format(self.name, self.pin)

    def generate_var_init_code(self):
        return '\nint {} = {};'.format(self.name, self.pin)


class Sensor(Brick):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return '\n\tpinMode({}, INPUT);'.format(self.name, self.pin)


class Actuator(Brick):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return '\n\tpinMode({}, OUTPUT);'.format(self.name, self.pin)


class AnalogicSensor(Sensor):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()


class DigitalSensor(Sensor):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def inline_read_code(self):
        return 'digitalRead({})'.format(self.name)


class DigitalActuator(Actuator):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()

    def assignment_code(self):
        return 'digitalWrite({})'.format(self.name)


class AnalogicActuator(Actuator):
    def __init__(self, parent, name, pin):
        super().__init__(parent, name, pin)

    def generate_setup_code(self):
        return super().generate_setup_code()


class LiquidCrystal(AnalogicActuator):
    def __init__(self, parent, name, pin, width, height):
        super().__init__(parent, name, pin)
        self.width = width
        self.height = height

    def generate_setup_code(self):
        return '\t\n{}.begin({}, {});'.format(self.name, self.width, self.height)


class Action(object):
    def __init__(self, parent):
        self.parent = parent


class Wait(object):
    def __init__(self, parent, milli):
        self.parent = parent
        self.milli = milli

    def __str__(self):
        return "\n\tdelay({});".format(self.milli)


class State(object):
    def __init__(self, parent, name, statements):
        self.parent = parent
        self.statements = statements
        self.name = name
        self.model_freq = self.parent._txa_frequency
        self.max_state_sleep = int(1000 * (1 / self.model_freq)) if self.model_freq > 0 else None

        action_exprs = list(filter(lambda e: type(e) is Action, self.statements))
        self.sleep_count_in_expr = sum([e.value if e.type == 'WAIT' else 0 for e in action_exprs])

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

        if no_transition:
            state_inner_code += "\tdelay({});".format(time_to_sleep_before_next_state)

        return state.format(name=self.name, code=state_inner_code)


class Transition(object):
    def __init__(self, parent, condition, next_state):
        self.parent = parent
        self.condition = condition
        self.next_state = next_state

    def generate_code(self, delay_before_next_state):
        if delay_before_next_state is not None and delay_before_next_state > 0:
            delay_instr = 'delay({});\n\t\t'.format(delay_before_next_state)

        else:
            delay_instr = ''

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


class Goto(object):
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
        return 'HIGH' if self.value == 'ON' else 'OFF'


class Assignment(object):
    def __init__(self, parent, var, new_value):
        self.parent = parent
        self.var = var
        self.new_value = new_value

    def __str__(self):
        return '{} = {}TOTO;'.format(self.var.name, self.new_value)


class AssignmentFromBrick(object):
    def __init__(self, parent, var, new_value):
        self.parent = parent
        self.var = var
        self.new_value = new_value

    def __str__(self):
        if type(self.new_value) is DigitalValue:
            return '{} = {};'.format(self.var.assignment_code(), self.new_value)
        else :
            return '{} = {};'.format(self.var.assignment_code(), self.new_value)

class Comparable(object):
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value

    def __str__(self):
        return '{}'.format(self.value)


class Condition(object):
    def __init__(self, parent, l, op, r):
        self.parent = parent
        self.l = l
        self.op = op
        self.r = r

    def __str__(self):
        return '{} {} {}'.format(self.l, self.op, self.r)

class ConditionTerm(object):
    def __init__(self, parent, l, op, r):
        self.parent = parent
        self.l = l
        self.op = op
        self.r = r

    def __str__(self):
        return '{} {} {}test'.format(self.l, self.op, self.r)

type_builtins = {
    'ON': DigitalValue(None, 'ON'),
    'OFF': DigitalValue(None, 'OFF'),
}

if __name__ == '__main__':

    classes = [Model, ConditionTerm, Transition, Wait, Action, DigitalActuator, Actuator, Assignment, AssignmentFromBrick,
               Condition, Comparable,
               Brick, State, DigitalValue, DigitalSensor,
               Goto]

    meta_model = tx.metamodel_from_file('grammar_v2.tx', classes=classes, builtins=type_builtins)
    try:
        os.mkdir('out')
    except FileExistsError:
        pass

    for file_name in os.listdir('samples2'):
        ID_REGISTRY = dict()
        READABLE_BRICK = dict()
        print("Translating {}".format(file_name))
        model = meta_model.model_from_file('samples2/{}'.format(file_name))

        out = open('out/{}'.format(file_name.replace('.aml', '.ino')), 'w')
        print(model, file=out)
        print(model)
        out.close()
