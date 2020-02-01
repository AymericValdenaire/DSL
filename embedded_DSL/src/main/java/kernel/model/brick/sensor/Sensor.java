package kernel.model.brick.sensor;

import kernel.model.brick.Brick;

public class Sensor extends Brick {

    public Sensor(String name, int pin) {
        super(name, pin);
    }

    @Override
    public String initCode() {
        return String.format("\n\tpinMode(%s, INPUT);", this.pin);
    }

}
