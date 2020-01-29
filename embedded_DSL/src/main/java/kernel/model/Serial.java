package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class Serial extends Brick {
  final int baudrate;

  public Serial(String name, int baudrate) {
    super(name);
    this.baudrate = baudrate;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String initCode() {
    return String.format("\tSerial.begin(%s);\n", this.baudrate);
  }

  @Override
  public String declarationVarCode() {
    return "";
  }

}
