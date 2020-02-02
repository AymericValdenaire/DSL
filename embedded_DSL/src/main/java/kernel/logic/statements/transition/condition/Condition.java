package kernel.logic.statements.transition.condition;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;
import lombok.Getter;

@Data
public class Condition implements Visitable {

  private Object left;
  private Object right;
  private String operator;

  public Condition(Object left, String operator, Object right) {
    this.left = left;
    this.right = right;
    this.operator = operator;
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
    return String.format("%s %s %s", left.toString(), operator, right.toString());
  }
}
