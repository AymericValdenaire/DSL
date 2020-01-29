package builder;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Permet d'initialiser une liste d'états
 */
public class StatesBuilder {
  @Getter(AccessLevel.PROTECTED)
  private ArduinoBuilder parent;

  private List<StateBuilder> states;

  public StatesBuilder(ArduinoBuilder parent) {
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
}
