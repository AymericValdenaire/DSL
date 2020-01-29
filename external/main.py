import textx as tx
import os

ID_REGISTRY = dict()
READABLE_BRICK = dict()

class Brick:
    def __init__(self, parent, name, child):
        self.parent = parent
        self.name = name
        self.child = child

        if self.name in ID_REGISTRY and type(self.parent) is Model:
            print('[ERROR] ID "{}" already declared'.format(self.name))
            exit(1)
        else:
            ID_REGISTRY[self.name] = self

        if type(self.child) in [Serial, Sensor]:
            if self.name in READABLE_BRICK and type(self.parent) is Model:
                print('[ERROR] ID "{}" already declared'.format(self.name))
                exit(1)
            else:
                READABLE_BRICK[self.name] = self.child

    def setup_code(self):
        return self.child.setup_code()

    def __str__(self):
        return str(self.child)

class Actuator:
    def __init__(self, parent, pin):
        self.parent = parent
        self.pin = pin

    def setup_code(self):
        return "pinMode({}, OUTPUT);".format(self.parent.name)

    def __str__(self):
        return "int {} = {};".format(self.parent.name, self.pin)

class Sensor:
    def __init__(self, parent, pin):
        self.parent = parent
        self.pin = pin

    def setup_code(self):
        return "pinMode({}, INPUT);".format(self.parent.name)

    def __str__(self):
        return "int {} = {};".format(self.parent.name, self.pin)

    def generate_read_code(self):
        return 'digitalRead({})'.format(self.pin)

class Serial:
    def __init__(self, parent, baudrate):
        self.parent = parent
        self.baudrate = baudrate

    def setup_code(self):
        return "Serial.begin({});".format(self.baudrate)

    def __str__(self):
        return "int {}_bytestream = 0;".format(self.parent.name)

class DigitalValue:
    def __init__(self, parent, value):
        self.parent=parent
        self.value=value

    def __str__(self):
        return 'HIGH' if self.value == 'ON' else "LOW"

class Action:
    def __init__(self, parent, type, var, value):
        self.parent = parent
        self.type = type
        self.var = var
        self.value = value

    def __str__(self):
        if self.type == "SET":
            return 'digitalWrite({}, {});\n'.format(self.var, self.value)

        if self.type == "PRINT":
            if type(ID_REGISTRY[self.var].child) is Lcd:
                if type(self.value) is str:
                    return '{lcd_name}.setCursor(0, 0);\n' \
                           '\t{lcd_name}.print("{value}");\n'.format(lcd_name=self.var, value=self.value.ljust(16))
                else:
                    if self.value in READABLE_BRICK:
                        return '{lcd_name}.setCursor(0, 0);\n' \
                               '\t{lcd_name}.print(prettyDigitalRead({value}));\n'.format(lcd_name=self.var, value=READABLE_BRICK[self.value].generate_read_code())
                    else:
                        return 'Serial.println("{}");\n'.format(self.value.ljust(16))

            if type(ID_REGISTRY[self.var].child) is Serial:
                if self.value in READABLE_BRICK:
                    return 'Serial.print(prettyDigitalRead({}));\n'.format(READABLE_BRICK[self.value].generate_read_code())
                else:
                    return 'Serial.print("{}");\n'.format(self.value.ljust(16))
            else:
                print("[ERROR] Unsupported brick type for PRINT : ",type(ID_REGISTRY[self.var].child))

        if self.type == "PRINTLN":
            if type(ID_REGISTRY[self.var].child) is Serial:
                if self.value in READABLE_BRICK:
                    return 'Serial.println(prettyDigitalRead({}));\n'.format(READABLE_BRICK[self.value].generate_read_code())
                else:
                    return 'Serial.println("{}");\n'.format(self.value.ljust(16))
            else:
                print("[ERROR] Unsupported brick type for PRINTLN : ",type(ID_REGISTRY[self.var].child))

        if self.type == "CLEAR":
            return '\n{lcd_name}.clear();\n'.format(lcd_name=self.var)

        if self.type == "WAIT":
            return 'delay({ms});\n'.format(ms=self.value)

class Transition:
    def __init__(self, parent, cond, next_state):
        self.parent = parent
        self.cond = cond
        self.next_state = next_state

    def generate_code(self, delay_before_next_state):
        if delay_before_next_state is not None and delay_before_next_state > 0:
            delay_instr = 'delay({});\n\t\t'.format(delay_before_next_state)
        else:
            delay_instr = ''

        next_state = self.next_state
        if type(next_state) is not Exception:
            next_state += '();'

        if self.cond.empty:
            return "{delay_instr}\n\t{next_state}".format(
                delay_instr=delay_instr,
                next_state=next_state)

        transition = open('templates/transition.amlt').read()
        return transition.format(condition=self.cond,
                                 next_state=next_state,
                                 delay_instr=delay_instr)

class Exception:
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value

    def __str__(self):
        return 'error({});'.format(self.value)


class State:
    def __init__(self, parent, is_init_state, name,exprs):
        self.parent = parent
        self.is_init_state = is_init_state
        self.name = name
        self.exprs = exprs

        self.model_freq = self.parent.__dict__['_txa_freq']
        self.max_state_sleep = int(1000 * (1 / self.model_freq)) if self.model_freq > 0 else None

        action_exprs = list(filter(lambda e: type(e) is Action, self.exprs))
        self.sleep_count_in_expr = sum([e.value if e.type == 'WAIT' else 0 for e in action_exprs])


    def __str__(self):
            state = open('templates/state.amlt').read()

            time_to_sleep_before_next_state = self.max_state_sleep
            state_inner_code = ''
            for expr in self.exprs:
                if type(expr) is Action:
                    if expr.type == "WAIT" and self.max_state_sleep is not None:
                        time_to_sleep_before_next_state -= expr.value
                    state_inner_code += '\t' + str(expr)
                if type(expr) is Transition:
                    state_inner_code += '\t' + expr.generate_code(time_to_sleep_before_next_state)

            if time_to_sleep_before_next_state is not None and time_to_sleep_before_next_state < 0 :
                print('[WARNING] Total wait actions {}ms exceed period {}ms in state "{}"'.format(self.sleep_count_in_expr,int(1000 / self.model_freq), self.name))

            return state.format(name=self.name, code=state_inner_code)

class Condition:
    def __init__(self,parent, l, r, op, single, empty):
        self.parent = parent
        self.l = l
        self.r = r
        self.op = op
        self.single = single
        self.empty = empty

    def __str__(self):
        if self.empty:
            return "true"
        if type(self.single) == Bexpr:
            return str(self.single)
        return '{} {} {}'.format(self.l, self.op, self.r)

class Bop:
    def __init__(self,parent, token):
        self.parent = parent
        self.token = token

    def __str__(self):
        ops = {'AND': '&&', 'OR': '||'}
        return ops[self.token]

class Bexpr:
    def __init__(self, parent, var,op, value):
        self.parent = parent
        self.var = var
        self.op = op
        self.value = value

    def __str__(self):
        if type(ID_REGISTRY[self.var].child) == Sensor:
            return '{} {} {}'.format(ID_REGISTRY[self.var].child.generate_read_code(), self.op, self.value)

        if type(ID_REGISTRY[self.var].child) == Serial:
            if type(self.value) is not str:
                print('[ERROR] Serial can only be compared with strings')
            if self.op == '==':
                return 'Serial.readString().indexOf("{}") >= 0'.format(self.value)
            else:
                print('[ERROR] Unsupported operator "{}" with serial read'.format(self.op))
                exit(1)
        else:
            print('[ERROR] Unsupported type within condition : {}'.format(type(self.var)))
            exit(1)
class Lcd:
    def __init__(self, parent,bus_id):
        self.parent = parent
        self.bus_id = bus_id

    def get_bus_pins(self):
        if self.bus_id == 1:
            return '2, 3, 4, 5, 6, 7, 8'
        if self.bus_id == 2:
            return '10, 11, 12, 13, 14, 15, 16'
        else:
            raise ValueError('Invalid Bus for lcd {} must be 1 or 2'.format(self.parent.name))

    def setup_code(self):
        return "{}.begin(16, 2);".format(self.parent.name)

    def __str__(self):
        return 'LiquidCrystal lcd({});'.format(self.get_bus_pins())


class Model(object):
    def __init__(self, bricks, states, freq):
        self.bricks = bricks
        self.states = states
        self.freq = freq

        self.max_state_sleep = int(1000 * (1 / freq)) if freq > 0 else None

        self.init_state = self.generate_loop_code()

    def generate_includes(self):
        out = set()
        for e in self.bricks:
            if type(e.child) == Lcd:
                out.add("LiquidCrystal.h")
        return '\n'.join(["#include <{}>".format(e) for e in out])

    def generate_setup_code(self):
        setup = open("templates/setup.amlt", 'r').read()
        return setup.format(setup_code='\n\t' + '\n\t'.join([e.setup_code() for e in self.bricks]))

    def generate_loop_code(self):
        init_states = list(map(lambda e: e.name,
                               list(filter(lambda e: e.is_init_state, self.states))))
        if len(init_states) != 1:
            raise SyntaxError("Invalid number of initial state")

        return 'void loop() {{ {init_state}(); }}'.format(init_state=init_states[0])

    def __str__(self):

        header = open("header.txt", 'r').read()
        return "{}\n{}\n{}\n{}\n{}\n{}".format(header,
                                               self.generate_includes(),
                                                '\n'.join([str(brick) for brick in self.bricks]),
                                                self.generate_setup_code(),
                                                '\n'.join([str(state) for state in self.states]),
                                                self.init_state)


if __name__ == '__main__':

    classes=[Model,Brick,Exception, Actuator, Sensor,Serial, DigitalValue, State, Transition, Action, Condition, Bop, Bexpr, Lcd]

    meta_model = tx.metamodel_from_file('grammar.tx', classes=classes)
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