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

  @Override
  public String toString(){
    
  }
}
