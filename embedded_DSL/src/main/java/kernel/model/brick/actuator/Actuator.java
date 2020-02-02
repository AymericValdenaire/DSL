package kernel.model.brick.actuator;

import kernel.model.brick.Brick;

public abstract class Actuator extends Brick {

    public Actuator(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String generateSetupCode() {
        return String.format("\tpinMode(%s, OUTPUT);\n", name);
    }

    public abstract String generateAssignementCode(String value);
}
