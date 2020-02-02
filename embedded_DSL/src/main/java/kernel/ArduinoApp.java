package kernel;

import java.util.ArrayList;
import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.State;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;
import lombok.Getter;

@Getter
public class ArduinoApp implements Visitable {

  private List<Brick> bricks;
  private List<State> stateMachine;
  private State intialState;
  private Serial serial;
  private int frequency;
  private int maxStateSleep;
  private String name;

  public ArduinoApp(String name){
    this.name = name;
    bricks = new ArrayList<Brick>();
    stateMachine =  new ArrayList<State>();
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String generateSetupCode() {
    return null;
  }

  @Override
  public String toString() {
    return null;
  }


}
