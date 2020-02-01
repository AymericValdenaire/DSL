package builder;

import builder.bool.BoolExpressionBuilder;
import builder.bool.BoolInstance;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de construire un état
 */
public class StateBuilder implements BoolInstance {

  private StatesBuilder parent;
  private String name;
  private List<BrickSetterBuilder> brickSetterBuilders = new ArrayList<>();
  private List<BoolExpressionBuilder> boolExpressions = new ArrayList<>();

  public StateBuilder(StatesBuilder parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  /**
   * Permet de d'initialiser des bricks setters
   *
   * @param name string brick name
   * @return BrickSetterBuilder
   */
  public BrickSetterBuilder set(String name) {
    BrickSetterBuilder brick = new BrickSetterBuilder(this, name);
    this.brickSetterBuilders.add(brick);
    return brick;
  }

  /**
   * Permet d'initialiser une boolean expression
   *
   * @return BoolExpression
   */
  public BoolExpressionBuilder when() {
    BoolExpressionBuilder boolExpression = new BoolExpressionBuilder(this);
    boolExpressions.add(boolExpression);
    return boolExpression;
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
