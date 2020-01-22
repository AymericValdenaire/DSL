package kernel.model;

import kernel.generator.Visitor;

public class Actuator extends Brick {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
