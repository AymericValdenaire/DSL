package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.model.DigitalValue;
import kernel.model.brick.Brick;
import lombok.Getter;

@Getter
public class BoolExpression implements Visitable {

  private final String var;
  private final String operator;
  private final Brick brick;
  private final DigitalValue digitalValue;

  public BoolExpression(String var, String operator, Brick brick) {
    this.var = var;
    this.digitalValue = null;
    this.operator = operator;
    this.brick = brick;
  }

  public BoolExpression(String var, String operator, DigitalValue digitalValue) {
    this.var = var;
    this.brick = null;
    this.operator = operator;
    this.digitalValue = digitalValue;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String generateSetupCode() {
    return "";
  }

  @Override
  public String generateCode() {
    return null;
  }


}
