package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;
import lombok.Getter;

@Data
public class Transition implements Visitable {
  State source;
  String sensor;
  String condition;
  State destination;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
