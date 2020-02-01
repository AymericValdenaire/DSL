package kernel;

import java.util.ArrayList;
import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.State;
import kernel.model.Brick;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ArduinoApp implements Visitable {

  private List<Brick> bricks;
  private List<State> stateMachine;
  private String name;

  public ArduinoApp(){
    bricks = new ArrayList<Brick>();
    stateMachine =  new ArrayList<State>();
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
