package kernel.logic.statements.transition.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition{

  private Object left;
  private Object right;
  private String operator;

  public Condition(Object left, String operator, Object right) {
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  public String generateSetupCode() {
    return "";
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", left.toString(), operator, right.toString());
  }
}
