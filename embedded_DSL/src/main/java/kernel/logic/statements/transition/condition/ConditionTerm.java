package kernel.logic.statements.transition.condition;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;
import lombok.Getter;

@Getter
public class ConditionTerm implements Visitable {

  private final Object right;
  private final Object left;
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
  public String toString() {
    if (operator == null) {
      return left.toString();
    } else {
      return String.format("%s %s %s", left.toString(), operator, right.toString());
    }
  }

}
