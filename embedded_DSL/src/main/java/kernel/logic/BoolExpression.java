package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;
import lombok.Getter;

@Data
public class BoolExpression implements Visitable {

  String expression;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
