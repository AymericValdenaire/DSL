package kernel.logic.statements.transition;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Getter;

@Getter
public class Exception implements Visitable {

  private int value;

  public Exception(int value){
    this.value = value;
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
    return String.format("error(%d)", value);
  }

}
