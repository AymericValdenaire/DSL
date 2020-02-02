package kernel.model.brick.actuator;

public class DigitalActuator extends Actuator{

  public DigitalActuator(String name, int pin) {
    super(name, pin);
  }

  @Override
  public String toString() {
    return String.format("digitalWrite(%s)", name);
  }

  @Override
  public String generateAssignementCode(String value) {
    return String.format("digitalWrite(%s, %s);", name,value);
  }

}
