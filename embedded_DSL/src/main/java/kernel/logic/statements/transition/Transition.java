package kernel.logic.statements.transition;

import kernel.logic.statements.Statement;
import kernel.logic.statements.transition.condition.Condition;

public class Transition extends Statement {

  private final Condition condition;
  private final String nextState;
  private final Exception exception;

  public Transition(Condition condition, String nextState, Exception exception) {
    this.condition = condition;
    this.nextState = nextState;
    this.exception = exception;
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
    if (this.exception == null) {
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

  @Override
  public String toString(){
    return "";
  }
}
