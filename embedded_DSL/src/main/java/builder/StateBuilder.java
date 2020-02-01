package builder;

import builder.bool.BoolExpressionBuilder;
import builder.bool.BoolInstance;
import builder.exception.ValidationException;
import kernel.logic.State;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de construire un état
 */
public class StateBuilder implements BoolInstance {

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
    return new BrickSetterBuilder(this, name);
  }

  /**
   * Permet d'initialiser une boolean expression
   *
   * @return BoolExpression
   */
  public BoolExpressionBuilder when() {
    return new BoolExpressionBuilder(this);
  }

  /**
   * Permet de continuer à instancer d'autre états sans avoir à faire un end-state
   *
   * @param name string state name
   * @return StateBuilder
   */
  public StateBuilder state(String name) {
    return parent.state(name);
  }

  /**
   * Permet de d'initialiser la state machine
   *
   * @return ArduinoApp
   */
  public StatesBuilder initState(String stateName) throws Exception {
    return parent.setInit(stateName);
  }

  @Override
  public StateBuilder getStateBuilder() {
    return this;
  }
}
