import textx as tx
import sys

class Model(object):
    def __init__(self, bricks, states):
        self.bricks = bricks
        self.states = states

        self.init_state = self.generate_loop_code()

    def generate_setup_code(self):
        setup = open("templates/setup.amlt", 'r').read()
        return setup.format(setup_code='\n\t'+ '\n\t'.join([e.setup_code() for e in self.bricks]))

    def generate_loop_code(self):
        init_states = list(map(lambda e : e.name,
                          list(filter(lambda e : e.is_init_state, self.states))))
        if len(init_states) != 1:
            raise SyntaxError("Invalid number of initial state")

        return 'void loop() {{ {init_state}(); }}'.format(init_state=init_states[0])

    def __str__(self):

        header = open("header.txt", 'r').read()
        return "{}\n{}\n{}\n{}\n{}".format(header,
                                           self.generate_setup_code(),
                                           '\n'.join([str(brick) for brick in self.bricks]),
                                           '\n'.join([str(state) for state in self.states]),
                                           self.init_state)

class Brick:
    def __init__(self, parent):
        self.parent = parent

class Actuator:
    def __init__(self, parent, name, pin):
        self.parent = parent
        self.name = name
        self.pin = pin

    def setup_code(self):
        return "pinMode({}, OUTPUT);".format(self.pin)

    def __str__(self):
        return "int {} = {};".format(self.name, self.pin)

class Sensor:
    def __init__(self, parent, name, pin):
        self.parent = parent
        self.name = name
        self.pin = pin

    def setup_code(self):
        return "pinMode({}, INPUT);".format(self.pin)

    def __str__(self):
        return "int {} = {};".format(self.name, self.pin)

class DigitalValue:
    def __init__(self, parent, value):
        self.parent=parent
        self.value=value

    def __str__(self):
        return 'HIGH' if self.value == 'ON' else "LOW"

class Action:
    def __init__(self, parent, var, value):
        self.parent = parent
        self.var = var
        self.value = value

    def __str__(self):
        return 'digitalWrite({}, {});\n'.format(self.var, self.value)

class Transition:
    def __init__(self, parent, cond, next_state):
        self.parent = parent
        self.cond = cond
        self.next_state = next_state

    def __str__(self):
        transition = open('templates/transition.amlt').read()
        return transition.format(test_var=self.cond.var,
                                 test_value=self.cond.value,
                                 next_state=self.next_state,
                                 current_state_name=self.parent.name)

class State:
    def __init__(self, parent, is_init_state, name,exprs):
        self.parent = parent
        self.is_init_state = is_init_state
        self.name = name
        self.exprs = exprs

    def __str__(self):
        state = open('templates/state.amlt').read()
        return state.format(name=self.name, code='\t'.join([str(expr) for expr in self.exprs]))

class Condition:
    def __init__(self, parent, var, value):
        self.parent = parent
        self.var = var
        self.value = value

def cname(o):
    return o.__class__.__name__

classes=[Model,Brick, Actuator, Sensor, DigitalValue, State, Transition, Action]

mmodel = tx.metamodel_from_file('grammar.tx', classes=classes)

if len(sys.argv) < 2 :
    print(mmodel.model_from_file('samples/scenario_1.aml'))
else:


    if len(sys.argv) > 1:
        model = mmodel.model_from_file(sys.argv[1])
        print(model)
        exit(0)

    data = sys.stdin.readlines()
    if len(data) > 0:

        for e in data:

            e = e.replace('\n', '')
            output_name = e.split('/')[-1].replace('.aml', '.ino')
            output_f = open('out/' + output_name, 'w')
            model = mmodel.model_from_file(e)
            print(model, file=output_f)