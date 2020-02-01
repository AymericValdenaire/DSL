package builder.bool;

import builder.BrickSetterBuilder;
import builder.StateBuilder;
import builder.exception.ValidationException;

/**
 * Permet de définir la condition d'une BoolExpression
 */
public class BoolSetterBuilder implements BoolInstance {

  private BoolExpressionBuilder parent;

  public BoolSetterBuilder(BoolExpressionBuilder parent) {
    this.parent = parent;
  }

  /**
   * Permet de set quand
   */
  public BrickSetterBuilder thenSet(String name) throws ValidationException {
    BrickSetterBuilder setter = new BrickSetterBuilder(this, name);
    this.parent.setAction(setter);
    return setter;
  }

  @Override
  public StateBuilder getStateBuilder() {
    return parent.getStateBuilder();
  }
}
