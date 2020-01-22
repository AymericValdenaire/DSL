package kernel.logic;

import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;
import lombok.Getter;

@Data
public class State implements Visitable {

  String name;
  List<Transition> transitions;

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
