package kernel.model.brick.sensor;

public class AnalogicSensor extends Sensor {

  public AnalogicSensor(String name, int pin) {
    super(name, pin);
  }

  @Override
  public String toString() {
    return String.format("analogRead(%s)", name);
  }

}
