package kernel.model.brick.sensor;

import kernel.model.brick.Brick;

public abstract class Sensor extends Brick {

    public Sensor(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String generateSetupCode() {
        return String.format("\tpinMode(%s, INPUT);\n", this.name);
    }

}
