import textx as tx

class Sensor(object):
    def __init__(self,parent, name, pins):
        self.parent = parent
        self.pins = pins
        self.name = name

    def __str__(self):
        return self.declaration_code()

    def declaration_code(self):
        out = ''
        if type(self.pins.data) is list:
            for i in range(len(self.pins.data)):
                out += 'int {}_{} = {};\n'.format(self.name, i, self.pins.data[i])
            return out
        return 'int {} = {};\n'.format(self.name, self.pins.data[0])

class Actuator(object):
    def __init__(self,parent, name, pins):
        self.parent = parent
        self.pins = pins
        self.name = name

    def __str__(self):
        return self.declaration_code()

    def declaration_code(self):
        out = ''
        if type(self.pins.data) is list:
            for i in range(len(self.pins.data)):
                out += 'int {}_{} = {};\n'.format(self.name, i, self.pins.data[i])
            return out
        return 'int {} = {};\n'.format(self.name, self.pins.data[0])

def cname(o):
    return o.__class__.__name__

classes=[Sensor, Actuator]

mmodel = tx.metamodel_from_file('grm.tx', classes=classes)

model = mmodel.model_from_file('./tests/samples/def.aml')

#
#For example, let’s consider the following scenario:
#“As a user, considering a button and a LED, I want to switch on the LED after pushing the button.
#Pushing the button again will switch it off, and so on”.
#
# The essence of ArduinoML is to support the definition of such an application.

for brick in model.brk:
    print(brick, end='')