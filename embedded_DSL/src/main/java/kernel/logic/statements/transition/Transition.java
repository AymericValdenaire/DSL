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
    String nextStateToMake;
    if (this.exception == null) {
      nextStateToMake = this.exception.generateSetupCode();
    } else {
      nextStateToMake = this.nextState + "();";
    }

    if (this.condition == null) {
      return String.format("{%s}\n\t{%s}", "delay_instr",nextStateToMake);
    } else {
      return String.format(
          "guard =  millis() - time > debounce;\n"
              + "    if ({%s}  && guard) {{\n"
              + "        time = millis();\n"
              + "        {%s}{%s}\n"
              + "    }}",
          this.condition.toString(),
          "delay_instr",
          nextStateToMake
      );
    }
  }

  @Override
  public String toString(){
    
  }
}
