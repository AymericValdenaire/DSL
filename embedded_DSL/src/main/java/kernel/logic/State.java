package kernel.logic;

import java.util.ArrayList;
import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.model.brick.sensor.Sensor;
import lombok.Data;

@Data
public class State implements Visitable {

  String name;
  Sensor sensor;
  List<Transition> transitions;

  public State(){
    transitions = new ArrayList<>();
  }
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
