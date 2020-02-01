package kernel.model.brick.sensor;

public class DigitalSensor extends Sensor{

  public DigitalSensor(String name, int pin) {
    super(name, pin);
  }

  public String readCode() {
    return String.format("digitalRead(%s)", name);
  }

}
