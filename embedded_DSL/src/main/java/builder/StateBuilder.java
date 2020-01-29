package builder;

import builder.bool.BoolInstance;
import java.util.List;
import kernel.ArduinoApp;
import lombok.Getter;

@Getter
public class StateBuilder implements BoolInstance{

  StatesBuilder parent;
  String name;
  List<BrickSetterBuilder> brickSetterBuilders;

  public StateBuilder(StatesBuilder parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  public BrickSetterBuilder set(String name) {
    return new BrickSetterBuilder(this, name);
  }

  public BoolExpression when() {
    return new BoolExpression(this);
  }

  public StateBuilder state(String name) {
    return parent.state(name);
  }

  public ArduinoApp build(){
    return parent.getParent().build();
  }

  @Override
  public StateBuilder getStateBuilder() {
    return this;
  }
}
