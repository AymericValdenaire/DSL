package builder;

import kernel.logic.statements.transition.Exception;
import kernel.logic.statements.transition.Transition;
import kernel.logic.statements.transition.condition.Condition;

/**
 * Permet de définir la condition d'une transition
 */
public class TransitionConditionOperationBuilder {

  private TransitionConditionBuilder parent;
  private Transition transition;

  public TransitionConditionOperationBuilder(TransitionConditionBuilder parent,Transition transition) {
    this.transition = transition;
    this.parent = parent;
  }

  public StatesBuilder thenGoToState(String stateName) {
    transition.setNextState(stateName);
    return parent.getParent().getParent();
  }

  public StatesBuilder thenError(int errorType){
    transition.setException(new Exception(errorType));
    return parent.getParent().getParent();
  }
  /**
   * To add a and condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder and() {
    transition.setCondition(new Condition(transition.getCondition() , "&&", null));
    return parent;
  }

  /**
   * To add a or condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder or() {
    transition.setCondition(new Condition(transition.getCondition() , "||", null));
    return parent;
  }

}
