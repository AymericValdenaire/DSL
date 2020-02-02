package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.State;
import kernel.logic.statements.Statement;
import kernel.logic.statements.action.Assignement;
import kernel.logic.statements.transition.Exception;
import kernel.logic.statements.transition.Transition;
import kernel.logic.statements.transition.condition.Condition;
import kernel.logic.statements.transition.condition.ConditionTerm;
import kernel.model.DigitalValue;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;

import java.util.List;

public class Generator extends Visitor<StringBuilder>{

  private StringBuilder builder;
  private List<State> stateMachine;

  public Generator(){
    this.builder = new StringBuilder();
  }

  @Override
  public void visit(Brick brick) {
    builder.insert(0, brick.toString() );
    builder.append(brick.generateSetupCode());
  }

  @Override
  public void visit(State currentstate) {
    builder.append(currentstate.generateSetupCode());
    Transition transition;
    for(int i = 0; i < currentstate.getTransitions().size();i++) {

      transition = currentstate.getTransitions().get(i);
      this.visit(currentstate.getTransitions().get(i));
      /*
      builder.append(String.format("if ( "));
      if(state.getName().equals("on")) {
        builder.append("HIGH");
      }else if(state.getName().equals("off")){
        builder.append("LOW");
      }
      builder.append(String.format(" == digitalRead(%s) && ",state.getSensor().getName()));
      state.getTransitions().get(i).accept(this);

       */

      for(State state : stateMachine){
        if(state.getName().equals(transition.getNextState())){
          for(int j = 0; j < currentstate.getStatements().size();j++) {
            this.visit(state.getStatements().get(j));
          }
        }
      }

    }

    builder.append("\n}");
  }

  @Override
  public void visit(Transition transition) {
    builder.append(transition.generateCode());
  }

  @Override
  public void visit(Assignement assignement) {
    builder.append(assignement.toString());
  }

  @Override
  public void visit(ArduinoApp arduinoApp) {
    this.stateMachine = arduinoApp.getStateMachine();
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
  public void visit(DigitalValue digitalValue) {

  }

  @Override
  public void visit(Serial serial) {

  }

  @Override
  public void visit(Exception e) {

  }

  @Override
  public void visit(Statement statement) {

  }

  @Override
  public void visit(ConditionTerm conditionTerm) {

  }

  @Override
  public void visit(Condition condition) {

  }


  public String getGeneratedCode(){
    return builder.toString();
  }

}
