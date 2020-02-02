package builder;

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

  /**
   * To add a and condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder and() {
    transition.setCondition(new Condition(transition.getCondition() , "&&", null));
    transition.getCondition().setOperator("and");
    return parent;
  }

  /**
   * To add a or condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder or() {
    transition.getCondition().setOperator("or");
    return parent;
  }

}
