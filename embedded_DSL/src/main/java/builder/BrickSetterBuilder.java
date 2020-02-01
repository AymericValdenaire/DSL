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
public class BrickSetterBuilder<T extends BoolInstance> {

  private T parent;
  private Brick brick;

  public BrickSetterBuilder(T parent, String brickName) throws ValidationException {
    this.parent = parent;

    this.brick = parent
        .getStateBuilder()
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
    return parent.getStateBuilder();
  }

  public StateBuilder toLow() {
    //local.setValue(SIGNAL.LOW);
    return parent.getStateBuilder();
  }

  public StateBuilder goUp() {
    //parent.local.getActions().add(this.local);
    return parent.getStateBuilder();
  }

}
