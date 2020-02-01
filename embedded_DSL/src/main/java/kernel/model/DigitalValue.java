package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;

public class DigitalValue implements Visitable {

  private final String value;

  public DigitalValue(String value) {
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String initCode() {
    return "";
  }

  @Override
  public String declarationVarCode() {
    return value;
  }
}
