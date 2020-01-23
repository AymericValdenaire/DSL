package kernel.model;

import kernel.generator.Visitor;

public class Sensor extends Brick {
    final int pin;

    public Sensor(String name, int pin) {
        super(name);
        this.pin = pin;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String declarationVarCode() {
        return null;
    }

    @Override
    public String initCode() {
        return String.format("\tpinMode(%s, INPUT);\n", this.pin);
    }
}
