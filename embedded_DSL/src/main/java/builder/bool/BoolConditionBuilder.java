package builder.bool;

import builder.BoolExpression;
import builder.BrickSetterBuilder;
import builder.StateBuilder;

public class BoolConditionBuilder implements BoolInstance{
  BoolExpression parent;
  boolean condition;

  public BoolConditionBuilder(BoolExpression parent, boolean condition) {
    this.parent = parent;
    this.condition = condition;
  }

  public BrickSetterBuilder thenSet(String name) {
    return new BrickSetterBuilder(this, name);
  }

  @Override
  public StateBuilder getStateBuilder() {
    return null;
  }
}
