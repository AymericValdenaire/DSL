package kernel.model;

import kernel.generator.Visitor;

public class Actuator extends Brick {

    final int pin;
    final Boolean isAnalogic;

    public Actuator(String name, int pin, Boolean isAnalogic) {
        super(name);
        this.pin = pin;
        this.isAnalogic = isAnalogic;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String declarationVarCode() {
        return String.format("int %s = %d;\n",this.name,this.pin);
    }

    @Override
    public String initCode() {
        return String.format("\tpinMode(%s, OUTPUT);\n", this.pin);
    }
}
