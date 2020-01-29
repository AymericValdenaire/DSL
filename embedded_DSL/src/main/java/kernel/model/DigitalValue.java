package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class DigitalValue implements Visitable, Representable {

  final String value;

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
