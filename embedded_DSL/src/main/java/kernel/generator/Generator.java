package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.State;
import kernel.logic.statements.action.Assignement;
import kernel.logic.statements.transition.Transition;
import kernel.model.DigitalValue;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;

public class Generator extends Visitor<StringBuilder>{

  private StringBuilder builder;

  public Generator(){
    this.builder = new StringBuilder();
  }

  @Override
  public void visit(Brick brick) {
    builder.insert(0, brick.toString());
    builder.append(brick.generateSetupCode());
  }

  @Override
  public void visit(State state) {
    for(int i = 0; i < state.getTransitions().size();i++) {

      builder.append(String.format("if ( "));
      if(state.getName().equals("on")) {
        builder.append("HIGH");
      }else if(state.getName().equals("off")){
        builder.append("LOW");
      }
      builder.append(String.format(" == digitalRead(%s) && ",state.getSensor().getName()));
      state.getTransitions().get(i).accept(this);
    }
  }

  @Override
  public void visit(ArduinoApp arduinoApp) {

    //setup
    builder.append("void setup()\n{\n");
    for (Brick brick: arduinoApp.getBricks()){
      brick.accept(this);
    }
    builder.append("}\n");

    //loop
    builder.append("void loop()\n{\n");
    for (State state: arduinoApp.getStateMachine()){
      state.accept(this);
    }
    builder.append("}\n");
  }

  @Override
  public void visit(Transition transition) {

  }

  @Override
  public void visit(DigitalValue digitalValue) {

  }

  @Override
  public void visit(Serial serial) {

  }

  @Override
  public void visit(Assignement assignement) {

  }

  public String getGeneratedCode(){
    return builder.toString();
  }

}
