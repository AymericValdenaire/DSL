package builder.bool;

import builder.BrickSetterBuilder;
import builder.StateBuilder;

/**
 * Permet de définir la condition d'une BoolExpression
 */
public class BoolSetterBuilder implements BoolInstance{
  BoolExpressionBuilder parent;

  public BoolSetterBuilder(BoolExpressionBuilder parent) {
    this.parent = parent;
  }

  /**
   * Permet de set quand
   * @param name
   * @return
   */
  public BrickSetterBuilder thenSet(String name) {
    BrickSetterBuilder setter = new BrickSetterBuilder(this, name);
    this.parent.setAction(setter);
    return setter;
  }

  @Override
  public StateBuilder getStateBuilder() {
    return parent.getStateBuilder();
  }
}
