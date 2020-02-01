package builder;

import builder.exception.ValidationException;
import kernel.logic.State;
import lombok.AccessLevel;
import lombok.Getter;

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

  public StateBuilder sleep(int time) {
    return this;
  }

  /**
   * Permet d'initialiser une boolean expression
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder when() {
    return new TransitionConditionBuilder(this);
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
