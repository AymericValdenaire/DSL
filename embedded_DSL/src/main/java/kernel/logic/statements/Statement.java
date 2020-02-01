package kernel.logic.statements;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public abstract class Statement implements Visitable {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
