package builder;

import builder.bool.BoolExpression;
import builder.bool.BoolInstance;
import java.util.ArrayList;
import java.util.List;
import kernel.ArduinoApp;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Permet de construire un état
 */
public class StateBuilder implements BoolInstance{

  @Getter(AccessLevel.PROTECTED)
  StatesBuilder parent;

  String name;
  List<BrickSetterBuilder> brickSetterBuilders = new ArrayList<>();
  List<BoolExpression> boolExpressions = new ArrayList<>();

  public StateBuilder(StatesBuilder parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  /**
   * Permet de d'initialiser des bricks setters
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
   * @return BoolExpression
   */
  public BoolExpression when() {
    BoolExpression boolExpression = new BoolExpression(this);
    boolExpressions.add(boolExpression);
    return boolExpression;
  }

  /**
   * Permet de continuer à instancer d'autre états sans avoir à faire un end-state
   * @param name string state name
   * @return StateBuilder
   */
  public StateBuilder state(String name) {
    return parent.state(name);
  }

  /**
   * Permet de build le arduino sans avoir a faire de end-state/end-states
   * @return ArduinoApp
   */
  public ArduinoApp build(){
    return parent.getParent().build();
  }

  @Override
  public StateBuilder getStateBuilder() {
    return this;
  }
}
