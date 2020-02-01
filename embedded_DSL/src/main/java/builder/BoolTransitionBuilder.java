package builder;

/**
 * Permet de définir la condition d'une BoolExpression
 */
public class BoolTransitionBuilder {

  private BoolExpressionBuilder parent;

  public BoolTransitionBuilder(BoolExpressionBuilder parent) {
    this.parent = parent;
  }

  public StatesBuilder thenGoToState(String stateName) {
    return parent.getParent().getParent();
  }

  /**
   * To add a and condition
   *
   * @return BoolExpressionBuilder
   */
  public BoolExpressionBuilder and() {
    return parent;
  }

  /**
   * To add a or condition
   *
   * @return BoolExpressionBuilder
   */
  public BoolExpressionBuilder or() {
    return parent;
  }

}
