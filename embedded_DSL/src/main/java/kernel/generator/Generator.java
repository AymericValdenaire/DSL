package kernel.generator;

import kernel.ArduinoCode;
import kernel.logic.State;
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
    builder.append("pinMode(").append(actuator.getPin()).append(", OUTPUT);\n");
  }

  @Override
  public void visit(Sensor sensor) {
    builder.append("pinMode(").append(sensor.getPin()).append(", INPUT);\n");
  }

  @Override
  public void visit(State state) {

  }

  @Override
  public void visit(ArduinoCode arduinoCode) {

    //setup
    builder.append("void setup()\n{\n");
    for (Brick brick: arduinoCode.getBricks()){
      brick.accept(this);
    }
    builder.append("}\n");

    //loop
    builder.append("void loop()\n{\n");
    for (State state: arduinoCode.getStateMachine()){
      state.accept(this);
    }
    builder.append("}\n");
  }

}
