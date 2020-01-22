import textx as tx
import sys

class Model(object):
    def __init__(self,gvar,  brk, exprBlock):
        self.gvar = gvar
        self.brk = brk
        self.exprBlock = exprBlock

    def __str__(self):
        out = self.generate_var_init()
        out += self.generate_var_setup()
        out += self.generate_loop_code()
        return out

    def freq_init_code(self):
        freq = self.get_global_var('FREQUENCY')
        if freq is not None:
            return str(freq)
        else:
            return 'float FREQUENCY 0.0;'

    def get_global_var(self, name):
        for var in self.gvar:
            if var.name == name:
                return var
        return None

    def generate_loop_code(self):
        out = open('templates/loop.tpl', 'r').read()
        return out.format(freq_init=self.freq_init_code(),
                          loop_code=''.join([str(expr) for expr in self.exprBlock]))

    def generate_var_init(self):
        return ''.join([brk.declaration_code() for brk in self.brk])

    def generate_var_setup(self):
        out = open('templates/VarSetup.tpl', 'r').read()
        return out.format(global_var='\n'.join([str(var) for var in self.gvar]),
                          setup_code='\t'.join([act.setup_code() for act in self.brk]))

class GlobalVar:
    def __init__(self, parent, name, val):
        self.parent = parent
        self.name = name
        self.val = val

    def __str__(self):
        if type(self.val) is int:
            return 'int {} = {};'.format(self.name, self.val)

class Wait:
    def __init__(self, parent, val):
        self.parent = parent
        self.val = val

    def __str__(self):
        if type(self.val) is int:
            return 'delay({});'.format(self.val)

class Attributes(object):
    def __init__(self, parent, data):
        self.parent = parent
        self.data = data

class Attribute(object):
    def __init__(self, parent, id, value, init):
        self.parent = parent
        self.id = id
        self.value = value
        self.init = init

class Sensor(object):
    def __init__(self,parent, name,atr):
        self.parent = parent
        self.atr = atr
        self.name = name

    def __str__(self):
        return self.declaration_code()

    def declaration_code(self):
        out = ''
        for i in range(len(self.atr.data)):
            out += 'int {}_{} = {};\n'.format(self.name, self.atr.data[i].id, self.atr.data[i].value)
        return out

    def setup_code(self):
        return ''.join(['pinMode({}_{}, INPUT);\n'.format(self.name, pins.id) for pins in self.atr.data])

class Actuator(object):
    def __init__(self,parent, name, atr):
        self.parent = parent
        self.atr = atr
        self.name = name

    def __str__(self):
        return self.declaration_code()

    def declaration_code(self):
        out = ''
        for i in range(len(self.atr.data)):
            out += 'int {}_{} = {};\n'.format(self.name, self.atr.data[i].id, self.atr.data[i].value)
            out += 'int {}_{}_state = {};\n'.format(self.name, self.atr.data[i].id,
                                                    self.atr.data[i].init[0] if len(self.atr.data[i].init) > 0 else 'LOW')
        return out

    def setup_code(self):
        return ''.join(['pinMode({}_{}, OUTPUT);\ndigitalWrite({}_{}, {});'.format(self.name, pins.id,
                                                                                   self.name, pins.id,
                                                                                   pins.init[0] if len(pins.init) > 0 else 'LOW')
                        for pins in self.atr.data])

class ExpressionBlock:
    def __init__(self,parent, cond, expr):
        self.parent = parent
        self.cond = cond
        self.expr = expr

    def __str__(self):
        template = open('templates/ExpressionBlock.tpl').read()
        return template.format(test=self.cond, expressions='\n'.join([str(e) for e in self.expr.data]))

class Bexprs:
    def __init__(self,parent,single, l, op, r):
        self.parent = parent
        self.single=single
        self.l = l
        self.op = op
        self.r = r

    def __str__(self):
        if self.single is not None :
            return str(self.single)
        if self.op == "AND":
            return '({} && {})'.format(self.r, self.l)
        if self.op == "OR":
            return '({} || {})'.format(self.r, self.l)

class Bexpr:
    def __init__(self, parent, trigger, state):
        self.parent = parent
        self.trigger = trigger
        self.state = state

    def __str__(self):
        return 'digitalRead({}) == {}'.format(self.trigger, self.state)

class State:
    def __init__(self, parent, val):
        self.parent = parent
        self.val = val

    def __str__(self):
        return self.val

class Bool:
    def __init__(self, parent, val):
        self.parent = parent
        self.val = val

    def __str__(self):
        return 'true' if self.val == 'TRUE' else 'false'

class Affectation:
    def __init__(self,parent, l,r):
        self.parent = parent
        self.l = l
        self.r = r

    def __str__(self):

        if type(self.r) is Bool:
            return '{} = {};'.format(self.l, self.r)

        if type(self.r) is State:
            return '{}_state = {};\n' \
                   'digitalWrite({}, {});'.format(self.l, self.r, self.l, self.r)

        if type(self.r) is Negation:
            return '{}_state = {}_state;\n' \
                   'digitalWrite({}, {}_state);'.format(self.l, self.r, self.l, self.r.var)

        return '{}_state = {}_state;\n' \
               'digitalWrite({}, {}_state);'.format(self.l, self.r,self.l, self.r)

class Negation:
    def __init__(self,parent, var):
        self.parent = parent
        self.var = var

    def __str__(self):
        return '!{}'.format(self.var)

class Var:
    def __init__(self, parent, brk, atr_name):
        self.parent = parent
        self.brk = brk
        self.atr_name = atr_name

    def __str__(self):
        return "{}_{}".format(self.brk, self.atr_name)

def cname(o):
    return o.__class__.__name__

classes=[Model,GlobalVar,Wait, Sensor, Actuator, Attributes, ExpressionBlock, Var, Affectation, Negation, Bexprs, Bexpr, State, Bool]

mmodel = tx.metamodel_from_file('grm.tx', classes=classes)

if len(sys.argv) < 2 :
    print("ERROR : Missing input file")
    exit(1)

HEADER = open("header.txt", 'r').read()

if len(sys.argv) > 1:
    model = mmodel.model_from_file(sys.argv[1])
    print(HEADER)
    print(model)
    exit(0)

data = sys.stdin.readlines()
if len(data) > 0:

    for e in data:

        e = e.replace('\n', '')
        output_name = e.split('/')[-1].replace('.aml', '.ino')
        output_f = open('out/' + output_name, 'w')
        model = mmodel.model_from_file(e)
        print(HEADER, file=output_f)
        print(model, file=output_f)