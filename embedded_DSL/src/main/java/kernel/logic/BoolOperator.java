package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class BoolOperator implements Visitable {

  final String operator;

  public BoolOperator(String operator) {
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
    return operator;
  }
}
