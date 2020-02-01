package kernel.logic;

import kernel.model.brick.Brick;

public class BrickBoolExpression extends AbstractBoolExpression{

  final Brick brick;

  public BrickBoolExpression(String var, BoolOperator operator, Brick brick) {
    super(var, operator);
    this.brick = brick;
  }

}
