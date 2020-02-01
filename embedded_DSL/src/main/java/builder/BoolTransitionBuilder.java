package builder;

import builder.exception.ValidationException;

/**
 * Permet de définir la condition d'une BoolExpression
 */
public class BoolTransitionBuilder implements BoolInstance {

  private BoolExpressionBuilder parent;

  public BoolTransitionBuilder(BoolExpressionBuilder parent) {
    this.parent = parent;
  }

  @Deprecated
  /**
   * Permet de set quand
   */
  public BrickSetterBuilder thenSet(String name) throws ValidationException {
    BrickSetterBuilder setter = new BrickSetterBuilder(this, name);
    this.parent.setAction(setter);
    return setter;
  }

  public StatesBuilder thenGoToState(String stateName) {
    return parent.getParent().getParent();
  }

  /**
   * To add a and condition
   *
   * @return BoolExpressionBuilder
   */
  public BoolExpressionBuilder and() {
    return parent;
  }

  /**
   * To add a or condition
   *
   * @return BoolExpressionBuilder
   */
  public BoolExpressionBuilder or() {
    return parent;
  }

  @Override
  public StateBuilder getStateBuilder() {
    return parent.getStateBuilder();
  }
}
