package kernel.logic;

import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.statements.Statement;
import kernel.logic.statements.action.Wait;
import kernel.logic.statements.transition.Transition;
import lombok.Getter;

@Getter
public class State implements Visitable {

  private final String name;
  private final List<Statement> statements;
  private final Float frequency;
  private final Integer maxStateSleep;

  public State(String name, List<Statement> statements, Float frequency){
    this.name = name;
    this.statements = statements;
    this.frequency = frequency;
    if (frequency > 0) {
      Float maxStateSleepFloat = (1000 * (1 / frequency));
      this.maxStateSleep = maxStateSleepFloat.intValue();
    } else {
      this.maxStateSleep = null;
    }
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    String state = "void {%s}()\n"
        + "{{\n"
        + "{%s} // No transition, loop on {%s} state"
        + "\t{%s}();\n"
        + "}}";

    Boolean noTransition = true;
    Integer sleepBeforeNextState = maxStateSleep;
    StringBuilder stateCodeBuilder = new StringBuilder();
    for(Statement current : statements) {
      if (current.getClass().equals(Wait.class) && maxStateSleep != null) {
        Wait currentWait = (Wait) current;
        sleepBeforeNextState -= currentWait.getMilli();
      }
      if (current.getClass().equals(Transition.class)) {
        noTransition = false;
        Transition currentTransition = (Transition) current;
        stateCodeBuilder.append("\n\t").append(currentTransition.generateCode(sleepBeforeNextState));
      } else {
        stateCodeBuilder.append("\n\t").append(current.toString());
      }
    }

    if(noTransition) {
      stateCodeBuilder.append("\tdelay(").append(sleepBeforeNextState.toString()).append(");");
    }

    return String.format(state, name, stateCodeBuilder.toString(), name, name);
  }

  @Override
  public String generateSetupCode() {
    return "";
  }
}
