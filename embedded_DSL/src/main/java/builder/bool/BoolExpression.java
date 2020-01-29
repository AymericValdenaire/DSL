package builder.bool;

import builder.StateBuilder;
import builder.bool.BoolConditionBuilder;

public class BoolExpression {

  StateBuilder stateBuilder;

  public BoolExpression(StateBuilder stateBuilder) {
    this.stateBuilder = stateBuilder;
  }

  public BoolConditionBuilder iff(boolean condition) {
    return new BoolConditionBuilder(this, condition);
  }
}
