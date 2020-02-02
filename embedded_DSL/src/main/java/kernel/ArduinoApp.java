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
  private State initialState;
  private Serial serial;
  private Float frequency;
  private int maxStateSleep;

  public ArduinoApp(List<Brick> bricks, List<State> states, State initialState, Serial serial, Float frequency){
    this.initialState = initialState;
    this.serial = serial;
    this.frequency = frequency;
    this.bricks = bricks;
    this.stateMachine =  states;
    Float maxStateSleepFloat = 1000 * (1 / frequency);
    this.maxStateSleep = maxStateSleepFloat.intValue();
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String generateLoopCode() {
    return String.format("void loop() {{ {%s}(); }}", initialState.getName());
  }

  public String generateVarInitCode() {
    StringBuilder builder = new StringBuilder();
    for (Brick currentBrick : bricks) {
      builder.append(currentBrick.toString());
    }
    return builder.toString();
  }

  @Override
  public String generateSetupCode() {
    return String.format("void setup()\n"
        + "{{{setup_code}\n"
        + "\n"
        + "    // Used for Exceptions\n"
        + "    pinMode(12, OUTPUT);\n"
        + "}}", serial.generateSetupCode());
  }

  public String generateStatesCode() {
    StringBuilder builder = new StringBuilder();
    for(State currentState : stateMachine) {
      builder.append("\n").append(currentState.toString());
    }
    return builder.toString();
  }

  /*public String generateIncludes() {
    String includes = "";
    for(Brick currentBrick : bricks) {

    }
  }*/

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    //builder.append(generateIncludes);
    builder.append(generateVarInitCode()).append(generateSetupCode()).append(generateStatesCode()).append(generateLoopCode());
    return builder.toString();
  }


}
