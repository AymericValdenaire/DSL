package builder;

import java.util.List;
import kernel.logic.BoolExpression;
import lombok.Getter;

@Getter
public class StatesBuilder {

  private ArduinoBuilder parent;
  private List<StateBuilder> stateBuilderList;

  public StatesBuilder(ArduinoBuilder parent) {
    this.parent = parent;
  }

  public StateBuilder state(String name) {
    return new StateBuilder(this, name);
  }
}
