package kernel.logic;

import java.util.ArrayList;
import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.statements.action.Assignement;
import kernel.logic.statements.transition.Transition;
import kernel.model.brick.sensor.Sensor;
import lombok.Data;

@Data
public class State implements Visitable {

  String name;
  Sensor sensor;
  List<Transition> transitions;
  List<Assignement> assignements;
  List<BoolExpression> boolExpressions;

  public State(){
    transitions = new ArrayList<>();
  }
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String generateSetupCode() {
    return null;
  }

  @Override
  public String generateCode() {
    return null;
  }
}
