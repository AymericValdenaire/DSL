package builder;

import builder.exception.ValidationException;
import kernel.logic.State;
import kernel.logic.statements.transition.Transition;
import kernel.logic.statements.transition.condition.Condition;
import kernel.logic.statements.transition.condition.ConditionTerm;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
/**
 * Permet de créer des expressions booléennes
 */
public class TransitionConditionBuilder {

  private StateBuilder parent;
  private String value;
  private Brick brick;
  private State currentState;
  private Transition transition;

  public TransitionConditionBuilder(StateBuilder stateBuilder, State state) {
    this.transition = new Transition();
    this.currentState = state;
    currentState.getTransitions().add(transition);
    this.parent = stateBuilder;
    this.currentState = state;
  }

  /**
   * Permet de définir la condition
   *
   * @param brickName String
   * @param value int
   * @return BoolSetterBuilder
   */
  public TransitionConditionOperationBuilder ifIsEqual(String brickName, String value) throws Exception {
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
    switch (value){
      case "ON":
        value = "HIGH";
        break;
      case "OFF":
        value = "LOW";
        break;
      default:
        value = "\""+value+"\"";
        break;
    }
    if(transition.getCondition() == null) {
        transition.setCondition(new Condition(brick , "==", value));
    }else{
      transition.getCondition().setRight(new Condition(brick , "==", value));
    }

    return new TransitionConditionOperationBuilder(this,transition);
  }


}
