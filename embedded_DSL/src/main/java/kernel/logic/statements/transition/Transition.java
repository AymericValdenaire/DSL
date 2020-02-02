package kernel.logic.statements.transition;

import kernel.logic.statements.Statement;
import kernel.logic.statements.transition.condition.Condition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transition extends Statement {

  private Condition condition;
  private String nextState;
  private final Exception exception;

  public Transition(Condition condition, String nextState, Exception exception) {
    this.condition = condition;
    this.nextState = nextState;
    this.exception = exception;
  }

  public Transition(){
    this.exception = null;
  }


  public String generateCode(Integer delay) {
    String delayInstr = "";

    if (delay != null && delay > 0) {
      delayInstr = String.format("delay(%d);\n\t\t", delay);
    }

    if (this.exception != null) {
      nextState = this.exception.toString();
    } /*else {
      nextState = exception.toString(); // c'est de la merde ?
    }*/

    if (nextState.getClass().equals(Exception.class)) {
      nextState += "();";
    }

    if (condition == null) {
      return String.format("{%s}\n\t{%s}", delayInstr, nextState);
    }

    return String.format(
            "guard =  millis() - time > debounce;\n\n"
                    + "    if (%s  && guard) {\n"
                    + "        time = millis();\n"
                    + "        %s();\n"
                    + "    }",
            String.format(this.condition.toString()),
            nextState
    );
  }

  public String generateCode(){
  if (this.exception != null) {
      nextState = this.exception.toString();
    } else {
      nextState = "";
    }
    if (condition == null) {
      return String.format("\n\t{%s}", nextState);
    } else {
      StringBuilder generatedCode = new StringBuilder("    if (");
      //conditions.forEach(condition -> generatedCode.append(condition.toString()));
      condition.toString();
      generatedCode.append(") {\n");
      return generatedCode.toString();
    }
  }

}
