package builder;

import java.util.ArrayList;
import java.util.List;
import kernel.ArduinoApp;
import kernel.logic.State;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Permet d'initialiser une liste d'états
 */
public class StatesBuilder {

  @Getter(AccessLevel.PROTECTED)
  private ArduinoBuilder parent;

  private List<State> states;
  private State initialState;

  public StatesBuilder(ArduinoBuilder parent) {
    this.states = new ArrayList<>();
    this.parent = parent;
  }

  /**
   * Permet d'initialiser un nouvel état
   *
   * @param name string nom de l'état
   */
  public StateBuilder state(String name) {
    State state = new State();
    state.setName(name);
    return new StateBuilder(this, state);
  }

  /**
   * Permet de d'initialiser la state machine
   *
   * @return ArduinoApp
   */
  public StatesBuilder initState(String stateName) throws Exception {
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
