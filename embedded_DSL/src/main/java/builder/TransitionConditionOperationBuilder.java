package builder;

/**
 * Permet de définir la condition d'une transition
 */
public class TransitionConditionOperationBuilder {

  private TransitionConditionBuilder parent;

  public TransitionConditionOperationBuilder(TransitionConditionBuilder parent) {
    this.parent = parent;
  }

  public StatesBuilder thenGoToState(String stateName) {
    return parent.getParent().getParent();
  }

  /**
   * To add a and condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder and() {
    return parent;
  }

  /**
   * To add a or condition
   *
   * @return TransitionConditionBuilder
   */
  public TransitionConditionBuilder or() {
    return parent;
  }

}
