package builder;

import java.util.ArrayList;
import java.util.List;
import kernel.ArduinoApp;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Permet d'initialiser une liste d'états
 */
public class StatesBuilder {
  @Getter(AccessLevel.PROTECTED)
  private ArduinoBuilder parent;

  private List<StateBuilder> states;
  private StateBuilder initialState;

  public StatesBuilder(ArduinoBuilder parent) {
    this.states = new ArrayList<>();
    this.parent = parent;
  }

  /**
   * Permet d'initialiser un nouvel état
   * @param name string nom de l'état
   * @return
   */
  public StateBuilder state(String name) {
    StateBuilder stateBuilder = new StateBuilder(this, name);
    this.states.add(stateBuilder);
    return stateBuilder;
  }

  protected StatesBuilder setInit(String stateName) throws Exception {
    this.initialState = states.stream()
        .filter(state -> state.getName().equals(stateName))
        .findFirst()
        .orElseThrow(() -> new Exception("State " + stateName + " not found"));

    return this;
  }

  public ArduinoApp build() {
    return this.parent.build();
  }

}
