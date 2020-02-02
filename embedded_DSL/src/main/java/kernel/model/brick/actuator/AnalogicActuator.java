package kernel.model.brick.actuator;

public class AnalogicActuator extends Actuator{

  public AnalogicActuator(String name, int pin) {
    super(name, pin);
  }

  @Override
  public String toString() {
    return String.format("analogRead(%s)", name);
  }

  @Override
  public String generateAssignementCode(String value) {
    return String.format("analogWrite(%s, %s);", name,value);
  }

}
