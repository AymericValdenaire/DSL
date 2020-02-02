package builder;

import builder.exception.ValidationException;
import kernel.logic.State;
import kernel.logic.statements.action.Print;
import kernel.model.brick.Serial;
import lombok.AccessLevel;
import lombok.Getter;

import static builder.ArduinoBuilder.serials;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de construire un état
 */
public class StateBuilder {

  private StatesBuilder parent;
  private State currentState;

  public StateBuilder(StatesBuilder parent, State state) {
    this.parent = parent;
    this.currentState = state;
  }

  /**
   * Permet de d'initialiser des bricks setters
   *
   * @param name string brick name
   * @return BrickSetterBuilder
   */
  public BrickSetterBuilder set(String name) throws ValidationException {
    return new BrickSetterBuilder(this, name, this.currentState);
  }

  public StateBuilder println(String name, String value) throws ValidationException {

    Serial serial = serials
            .stream()
            .filter(actuator -> actuator.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new ValidationException("Brick of name " + name + " is not found or is not a actuator"));
    currentState.getStatements().add(new Print("PRINTLN", serial,value));
    return this;
  }

  public StateBuilder sleep(int time) {
    return this;
  }

  /**
   * Permet d'initialiser une boolean expression
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder when() {
    return new TransitionConditionBuilder(this,currentState);
  }

  /**
   * Permet de continuer à instancer d'autre états sans avoir à faire un end-state
   *
   * @param name string state name
   * @return StateBuilder
   */
  public StateBuilder state(String name) throws ValidationException {
    return parent.state(name);
  }

}
