package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public abstract class AbstractBoolExpression implements Visitable {

  final String var;
  final BoolOperator operator;

  protected AbstractBoolExpression(String var, BoolOperator operator) {
    this.var = var;
    this.operator = operator;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String initCode() {
    return "";
  }

  @Override
  public String declarationVarCode() {
    return "";
  }
}
