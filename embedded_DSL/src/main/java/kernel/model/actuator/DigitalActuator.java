package kernel.model.actuator;

public class DigitalActuator extends Actuator{

  public DigitalActuator(String name, int pin) {
    super(name, pin);
  }

  @Override
  public String initCode() {
    return String.format("digitalWrite(%s)", name);
  }

}
