package builder;

import builder.exception.ValidationException;
import kernel.model.brick.Brick;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de set des états au bricks
 * @param <T> Instance de BoolInstance
 */
public class BrickSetterBuilder {

  private StateBuilder parent;
  private Brick brick;

  public BrickSetterBuilder(StateBuilder parent, String brickName) throws ValidationException {
    this.parent = parent;

    this.brick = parent
        .getParent()
        .getParent()
        .getArduinoApp()
        .getBricks()
        .stream()
        .filter(brick -> brick.getName().equals(brickName))
        .findFirst()
        .orElseThrow(() -> new ValidationException("Brick of name " + brickName + " is not found"));
  }

  public StateBuilder toHigh() {
    //local.setValue(SIGNAL.HIGH);
    return parent;
  }

  public StateBuilder toLow() {
    //local.setValue(SIGNAL.LOW);
    return parent;
  }

  public StateBuilder goUp() {
    //parent.local.getActions().add(this.local);
    return parent;
  }

}
