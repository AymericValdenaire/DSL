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

  private TransitionConditionOperationBuilder conditionbuilder(String brickName, String value,String op)throws Exception{
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
        try {
          Integer.parseInt(value);
        } catch (NumberFormatException e) {
          value = "\""+value+"\"";
        }

        break;
    }
    if(brick instanceof Serial){
      if(op.equals(">") || op.equals("<")){
        throw new Exception("Greater and Lower condition are not available for serial");
      }
      if(transition.getCondition() == null) {
        transition.setCondition(new Condition(brick.toString()+"indexOf("+value+")" , ">=", "0"));
      }else{
        transition.getCondition().setRight(new Condition(brick.toString()+"indexOf("+value+")" , ">=", "0"));
      }
    }else{
      if(transition.getCondition() == null) {
        transition.setCondition(new Condition(brick , op, value));
      }else{
        transition.getCondition().setRight(new Condition(brick , op, value));
      }
    }

    return new TransitionConditionOperationBuilder(this,transition);
  }
  /**
   * Permet de définir la condition
   *
   * @param brickName String
   * @param value int
   * @return BoolSetterBuilder
   */
  public TransitionConditionOperationBuilder ifIsEqual(String brickName, String value) throws Exception {
    return conditionbuilder(brickName, value,"==");
  }

  public TransitionConditionOperationBuilder ifIsGreaterThan(String brickName, String value) throws Exception {
    return conditionbuilder(brickName, value, ">");
  }

  public TransitionConditionOperationBuilder ifIsLowerThan(String brickName, String value) throws Exception {
    return conditionbuilder(brickName, value, "<");
  }

}
