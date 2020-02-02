package kernel;

import java.util.ArrayList;
import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.State;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;
import kernel.model.brick.actuator.LiquidCrystal;
import lombok.Data;

@Data
public class ArduinoApp implements Visitable {

  private List<Brick> bricks;
  private List<State> stateMachine;
  private String initialState;
  private Serial serial;
  private Float frequency;
  private int maxStateSleep;
  private String name;

  public ArduinoApp(List<Brick> bricks, List<State> states, String initialState, Serial serial, Float frequency){
    this.initialState = initialState;
    this.serial = serial;
    this.frequency = frequency;
    this.bricks = bricks;
    this.stateMachine =  states;
    Float maxStateSleepFloat = 1000 * (1 / frequency);
    this.maxStateSleep = maxStateSleepFloat.intValue();
  }

  public ArduinoApp(){
    bricks = new ArrayList<>();
    stateMachine = new ArrayList<>();
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String generateLoopCode() {
   /* if(initialState == null){
      throw new Exception("you must define an initial state");
    }*/
    return String.format("\nvoid loop() { %s(); }", initialState);
  }

  public String generateVarInitCode() {
    StringBuilder builder = new StringBuilder();
    for (Brick currentBrick : bricks) {
      builder.append(currentBrick.declarationVariableCode());
    }
    return builder.toString();
  }

  @Override
  public String generateSetupCode() {
    StringBuilder stringBuilder = new StringBuilder(String.format("\n"
        + "long time = 0;\n"
        + "long debounce = 200;\n"
        + "\n"
        + "boolean guard;\n\n"
        + "void error(int n)\n"
        + "{\n"
        + "    for(int c = 0 ; c != n ; c++)\n"
        + "    {\n"
        + "        digitalWrite(12, HIGH);\n"
        + "        delay(200);\n"
        + "        digitalWrite(12, LOW);\n"
        + "        delay(200);\n"
        + "    }\n"
        + "    delay(200 * n);\n"
        + "    error(n);\n"
        + "}\n"
        + "\n"
        + "String prettyDigitalRead(String name, int value)\n"
        + "{\n"
        + "    if (value == 0)\n"
        + "    {\n"
        + "        return (name + \" := ON\");\n"
        + "    }\n"
        + "    return (name + \" := OFF\");\n"
        + "}\n"
        + "\n"
        + "String prettyAnalogPrint(String name, float value)\n"
        + "{\n"
        + "    return (name + \" := \"+ value);\n"
        + "}\n"
        + "\n"
        + "void setup()\n"
        + "{%s\n"
        + "\n"
        + "    // Used for Exceptions\n"
        + "    pinMode(12, OUTPUT);\n"
        + "", (serial != null) ? serial.generateSetupCode() : "" ));
    for (Brick currentBrick : bricks) {
      stringBuilder.append(currentBrick.generateSetupCode());
    }
    stringBuilder.append( "}");
    return stringBuilder.toString();
  }

  public String generateStatesCode() {
    StringBuilder builder = new StringBuilder();
    for(State currentState : stateMachine) {
      builder.append("\n").append(currentState.toString());
    }
    return builder.toString();
  }

  public String generateIncludes() {
    StringBuilder includes = new StringBuilder();
    for(Brick currentBrick : bricks) {
      if(currentBrick.getClass().equals(LiquidCrystal.class)){
        LiquidCrystal lcd = (LiquidCrystal) currentBrick;
        for (String include : lcd.dependencies()) {
          includes.append(String.format("#include <%s.h>\n", include));
        }
      }
    }
    return includes.toString();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(generateIncludes());
    builder.append(generateVarInitCode()).append(generateSetupCode()).append(generateStatesCode()).append(generateLoopCode());
    return builder.toString();
  }

}
