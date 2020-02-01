package builder;

import builder.exception.ValidationException;
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

  private State initialState;

  public StatesBuilder(ArduinoBuilder parent) {
    this.parent = parent;
  }

  /**
   * Permet d'initialiser un nouvel état
   *
   * @param name string nom de l'état
   */
  public StateBuilder state(String name) throws ValidationException {
    if (this.parent.getArduinoApp().getStateMachine().stream()
        .anyMatch(state -> state.getName().equals(name))) {
      throw new ValidationException("State " + name + " is already defined");
    }
    State state = new State();
    state.setName(name);
    this.parent.getArduinoApp().getStateMachine().add(state);
    return new StateBuilder(this, state);
  }

  /**
   * Permet de d'initialiser la state machine
   *
   * @return ArduinoApp
   */
  public StatesBuilder initState(String stateName) throws Exception {
    this.initialState = this.parent.getArduinoApp().getStateMachine().stream()
        .filter(state -> state.getName().equals(stateName))
        .findFirst()
        .orElseThrow(() -> new Exception("State " + stateName + " not found"));

    return this;
  }

  public ArduinoApp build() {
    return this.parent.build();
  }

}
