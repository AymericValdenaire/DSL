package builder;

import builder.bool.BoolInstance;

public class BrickSetterBuilder<T extends BoolInstance> {

  T parent;
  String brick;

  public BrickSetterBuilder(T parent, String brick) {
    this.parent = parent;
    this.brick = brick;
  }

  public StateBuilder toHigh() {
    //local.setValue(SIGNAL.HIGH);
    return parent.getStateBuilder();
  }

  public StateBuilder toLow() {
    //local.setValue(SIGNAL.LOW);
    return parent.getStateBuilder();
  }

  private StateBuilder goUp() {
    //parent.local.getActions().add(this.local);
    return parent.getStateBuilder();
  }

}
