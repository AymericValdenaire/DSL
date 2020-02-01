package builder;

import builder.exception.ValidationException;
import kernel.model.brick.Brick;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de créer des expressions booléennes
 */
public class BoolExpressionBuilder {

  private StateBuilder parent;
  private String value;
  private Brick brick;

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
    this.brick = parent
        .getParent()
        .getParent()
        .getArduinoApp()
        .getBricks()
        .stream()
        .filter(brick -> brick.getName().equals(brickName))
        .findFirst()
        .orElseThrow(() -> new ValidationException("Brick of name " + brickName + " is invalid"));
    this.value = value;
    return new BoolTransitionBuilder(this);
  }
}
