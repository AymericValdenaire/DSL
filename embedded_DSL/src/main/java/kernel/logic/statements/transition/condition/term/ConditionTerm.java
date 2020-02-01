package kernel.logic.statements.transition.condition.term;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;

public class ConditionTerm implements Visitable {

  private final Object right;
  private final Brick left;
  private final String operator;

  public ConditionTerm(Brick left, String operator, Object right) {
    this.right = right;
    this.left = left;
    this.operator = operator;
  }

  public ConditionTerm(Object right, Serial serial) {
    this.right = right;
    this.left = serial;
    this.operator = null;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String generateSetupCode() {
    return "";
  }

  @Override
  public String generateCode() {
    if (operator == null) {
      return String.format("%s.indexOf(\"%s\") >= 0");
    } else {
      return String.format("%s %s %s", left.generateCode(), operator, right.toString());
    }
  }

}
