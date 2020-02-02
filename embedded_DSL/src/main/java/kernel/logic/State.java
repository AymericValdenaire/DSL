package kernel.logic;

import java.util.ArrayList;
import java.util.List;
import kernel.logic.statements.Statement;
import kernel.logic.statements.action.Wait;
import kernel.logic.statements.transition.Transition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {

  private String name;
  private List<Transition> transitions;
  private List<Statement> statements;
  private Float frequency;
  private Integer maxStateSleep;

  public State(){
    this.transitions = new ArrayList<>();
    this.statements = new ArrayList<>();
  }

  public State(String name, List<Statement> statements, Float frequency){
    this.transitions = new ArrayList<>();
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

  @Override
  public String toString() {
    String state = "void %s()"
        + "{\n\n%s // No transition, loop on {%s} state\n"
        + "\t%s();\n"
        + "}\n";

    Boolean noTransition = true;
    Integer sleepBeforeNextState = maxStateSleep;
    StringBuilder stateCodeBuilder = new StringBuilder();
    for(Statement current : statements) {
      if (current.getClass().equals(Wait.class) && maxStateSleep != null) {
        Wait currentWait = (Wait) current;
        sleepBeforeNextState -= currentWait.getMilli();
      }else {
        stateCodeBuilder.append("\t"+current.toString()+"\n");
      }
    }

    for(Transition transition : transitions) {
      if (transition.getClass().equals(Transition.class)) {
        noTransition = false;
        stateCodeBuilder.append("\n\t").append(transition.generateCode(sleepBeforeNextState));
      } else {
        stateCodeBuilder.append("\n\t").append(transition.toString());
      }
    }

    if(noTransition){
      stateCodeBuilder.append("\tdelay(").append(sleepBeforeNextState).append(");");
    }
    return String.format(state, name, stateCodeBuilder.toString(), name, name);
  }
}
