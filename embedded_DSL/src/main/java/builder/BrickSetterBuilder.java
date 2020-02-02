package builder;

import static builder.ArduinoBuilder.analogicActuators;
import static builder.ArduinoBuilder.digitalActuators;

import builder.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import kernel.logic.State;
import kernel.logic.statements.action.Assignement;
import kernel.model.brick.actuator.Actuator;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de set des états au bricks
 * @param <T> Instance de BoolInstance
 */
public class BrickSetterBuilder {

  private StateBuilder parent;
  private Actuator actuator;
  private State state;

  public BrickSetterBuilder(StateBuilder parent, String actuatorName, State state) throws ValidationException {
    this.parent = parent;
    this.state = state;

    List<Actuator> actuators = new ArrayList<>();
    actuators.addAll(analogicActuators);
    actuators.addAll(digitalActuators);

    this.actuator = actuators
        .stream()
        .filter(actuator -> actuator.getName().equals(actuatorName))
        .findFirst()
        .orElseThrow(() -> new ValidationException("Brick of name " + actuatorName + " is not found or is not a actuator"));
  }

  public StateBuilder toHigh() {
    state.getStatements().add(new Assignement(actuator, Signal.HIGH));
    return parent;
  }

  public StateBuilder toLow() {
    state.getStatements().add(new Assignement(actuator, Signal.LOW));
    return parent;
  }

  public StateBuilder goUp() {
    //parent.local.getActions().add(this.locxal);
    return parent;
  }

  public StateBuilder to(String action) {
    state.getStatements().add(new Assignement(actuator, action));
    return parent;
  }

}
