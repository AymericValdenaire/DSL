package kernel.logic.statements.transition;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.State;
import kernel.logic.statements.Statement;
import lombok.Data;
import lombok.Getter;

public class Transition extends Statement {

  String condition;
  State destination;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String initCode() {
    return null;
  }

  @Override
  public String declarationVarCode() {
    return null;
  }
}
