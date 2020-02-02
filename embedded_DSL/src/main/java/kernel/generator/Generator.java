package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.State;


import java.util.List;

public class Generator extends Visitor<StringBuilder>{

  private StringBuilder builder;
  private List<State> stateMachine;

  public Generator(){
    this.builder = new StringBuilder();
  }

  @Override
  public void visit(ArduinoApp arduinoApp) {
    builder.append(arduinoApp.toString());
  }

  public String getGeneratedCode(){
    return builder.toString();
  }

}
