package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.model.Brick;
import kernel.model.DigitalValue;
import lombok.Getter;

@Getter
public class BoolExpression implements Visitable {

  final String var;
  final BoolOperator operator;
  final Brick brick;
  final DigitalValue digitalValue;

  public BoolExpression(String var, BoolOperator operator, Brick brick) {
    this.var = var;
    this.digitalValue = null;
    this.operator = operator;
    this.brick = brick;
  }

  public BoolExpression(String var, BoolOperator operator, DigitalValue digitalValue) {
    this.var = var;
    this.brick = null;
    this.operator = operator;
    this.digitalValue = digitalValue;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String initCode() {
    return "";
  }

  @Override
  public String declarationVarCode() {
    return null;
  }


}
