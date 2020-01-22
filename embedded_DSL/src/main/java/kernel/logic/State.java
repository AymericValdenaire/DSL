package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class State implements Visitable {
  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
