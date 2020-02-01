package kernel.model.actuator;

import kernel.generator.Visitor;
import kernel.model.Brick;

public class Actuator extends Brick {

    public Actuator(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String initCode() {
        return String.format("\n\tpinMode(%s, OUTPUT);", this.pin);
    }
}
