package kernel.logic.statements.transition;

import kernel.logic.statements.Statement;
import kernel.logic.statements.transition.condition.Condition;
import lombok.Data;

@Data
public class Transition extends Statement {

  private Condition condition;
  private String nextState;
  private Exception exception;

  public Transition(Condition condition, String nextState, Exception exception) {
    this.condition = condition;
    this.nextState = nextState;
    this.exception = exception;
  }

  public Transition(){
    exception = null;
  }

  @Override
  public String generateSetupCode() {
    return "";
  }

  public String generateCode(Integer delay) {
    String delayInstruction;
    if (delay != null && delay > 0) {
      delayInstruction = String.format("delay(%s);\n\t\t", delay);
    } else {
      delayInstruction = "";
    }

    String nextStateInstruction;
    if (this.exception != null) {
      nextStateInstruction = this.exception.generateSetupCode();
    } else {
      nextStateInstruction = this.nextState + "();";
    }

    if (this.condition == null) {
      return String.format("%s\n\t%s", delayInstruction,nextStateInstruction);
    } else {
      return String.format(
          "guard =  millis() - time > debounce;\n"
              + "    if (%s  && guard) {{\n"
              + "        time = millis();\n"
              + "        %s%s\n"
              + "    }}",
          this.condition.toString(),
          delayInstruction,
          nextStateInstruction
      );
    }
  }

  public String generateCode() {

    String nextStateInstruction;
    if (this.exception != null) {
      nextStateInstruction = this.exception.generateSetupCode();
    } else {
      nextStateInstruction = this.nextState + "();";
    }

    if (this.condition == null) {
      return String.format("\n\t%s", nextStateInstruction);
    } else {
      return String.format(
                      "    if ("+this.condition.toString()+") {\n"
      );
    }
  }
  @Override
  public String toString(){
    return "";
  }
}
