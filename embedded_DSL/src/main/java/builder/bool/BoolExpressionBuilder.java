package builder.bool;

import builder.BrickSetterBuilder;
import builder.StateBuilder;
import builder.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de créer des expressions booléennes
 */
public class BoolExpressionBuilder implements BoolInstance{

  private StateBuilder parent;
  private String value;

  @Setter(AccessLevel.PROTECTED)
  BrickSetterBuilder action;

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
  public BoolSetterBuilder ifIsEqual(String brickName, String value) throws ValidationException {
    this.action = new BrickSetterBuilder(this, brickName);
    this.value = value;
    return new BoolSetterBuilder(this);
  }

  @Override
  public StateBuilder getStateBuilder() {
    return this.parent;
  }
}
