package kernel.model.brick.actuator;

import kernel.model.brick.Brick;

public abstract class Actuator extends Brick {

    public Actuator(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String generateSetupCode() {
        return String.format("\n\tpinMode(%d, OUTPUT);", pin);
    }

    public abstract String generateAssignementCode();
}
