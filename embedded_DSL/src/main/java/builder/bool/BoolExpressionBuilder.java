package builder.bool;

import builder.BrickSetterBuilder;
import builder.StateBuilder;
import lombok.AccessLevel;
import lombok.Setter;


/**
 * Permet de créer des expressions booléennes
 */
public class BoolExpressionBuilder {

  StateBuilder stateBuilder;
  String brickName;
  String value;

  @Setter(AccessLevel.PROTECTED)
  BrickSetterBuilder action;

  public BoolExpressionBuilder(StateBuilder stateBuilder) {
    this.stateBuilder = stateBuilder;
  }

  /**
   * Permet de définir la condition
   * @param brickName String
   * @param value int
   * @return BoolSetterBuilder
   */
  public BoolSetterBuilder ifIsEqual(String brickName, String value) {
    this.brickName = brickName;
    this.value = value;
    return new BoolSetterBuilder(this);
  }
}
