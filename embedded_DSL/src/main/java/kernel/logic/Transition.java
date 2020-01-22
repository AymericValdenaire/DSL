package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Getter;

@Getter
public class Transition implements Visitable {

  BoolExpression condition;
  State destination;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
