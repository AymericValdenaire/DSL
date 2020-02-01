package kernel.model.brick.actuator;

import kernel.model.brick.Brick;

public class Actuator extends Brick {

    public Actuator(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String initCode() {
        return String.format("\n\tpinMode(%s, OUTPUT);", this.pin);
    }
}
