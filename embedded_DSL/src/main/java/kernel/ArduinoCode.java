package kernel;

import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.State;
import kernel.model.Brick;
import lombok.Getter;

@Getter
public class ArduinoCode implements Visitable {

  private List<Brick> bricks;
  private List<State> stateMachine;

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
