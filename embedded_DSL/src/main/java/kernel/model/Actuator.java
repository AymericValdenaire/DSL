package kernel.model;

import kernel.generator.Visitor;


public class Actuator extends Brick {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String initCode() {
        return String.format("pinMode(%s, OUTPUT);\n", this.getPin());
    }
}
