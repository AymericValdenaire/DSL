package kernel.model.brick.sensor;

public class DigitalSensor extends Sensor{

  public DigitalSensor(String name, int pin) {
    super(name, pin);
  }

  @Override
  public String toString() {
    return String.format("digitalRead(%s)", name);
  }

}
