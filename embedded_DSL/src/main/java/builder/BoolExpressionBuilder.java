package builder;

import builder.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de créer des expressions booléennes
 */
public class BoolExpressionBuilder implements BoolInstance{

  private StateBuilder parent;
  private String value;
  private BrickSetterBuilder action;

  public BoolExpressionBuilder(StateBuilder stateBuilder) {
    this.parent = stateBuilder;
  }

  /**
   * Permet de définir la condition
   *
   * @param brickName String
   * @param value int
   * @return BoolSetterBuilder
   */
  public BoolTransitionBuilder ifIsEqual(String brickName, String value) throws ValidationException {
    this.action = new BrickSetterBuilder(this, brickName);
    this.value = value;
    return new BoolTransitionBuilder(this);
  }

  @Override
  public StateBuilder getStateBuilder() {
    return this.parent;
  }
}
