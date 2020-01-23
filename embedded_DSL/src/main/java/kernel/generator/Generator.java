package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;
import kernel.model.Actuator;
import kernel.model.Brick;
import kernel.model.Sensor;

public class Generator extends Visitor<StringBuilder>{

  private StringBuilder builder;

  public Generator(){
    this.builder = new StringBuilder();
  }

  @Override
  public void visit(Actuator actuator) {
    builder.insert(0,String.format("int %s = %d;\n",actuator.getName(),actuator.getPin()));
    builder.append("pinMode(").append(actuator.getPin()).append(", OUTPUT);\n");
  }

  @Override
  public void visit(Sensor sensor) {

    builder.insert(0,String.format("int %s = %d;\n",sensor.getName(),sensor.getPin()));
    builder.append("pinMode(").append(sensor.getPin()).append(", INPUT);\n");
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

    builder.append(String.format("digitalRead(%s) == %s ) { \n",transition.getSensor(),transition.getCondition()));

    builder.append(String.format("digitalWrite( %s, ",transition.getDestination().getSensor().getName()));
    if(transition.getDestination().getName().equals("on")) {
      builder.append("HIGH");
    }else if(transition.getDestination().getName().equals("off")){
      builder.append("LOW");
    }
    builder.append(" ); \n}\n");
  }

  @Override
  public void visit(BoolExpression boolExpression) {

  }

  public String getGeneratedCode(){
    return builder.toString();
  }

}
