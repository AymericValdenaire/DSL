package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.State;
import kernel.logic.statements.Statement;
import kernel.logic.statements.action.Assignement;
import kernel.logic.statements.transition.Exception;
import kernel.logic.statements.transition.Transition;
import kernel.logic.statements.transition.condition.Condition;
import kernel.logic.statements.transition.condition.ConditionTerm;
import kernel.model.DigitalValue;
import kernel.model.brick.Brick;
import kernel.model.brick.Serial;

public abstract class Visitor<T> {

  public abstract void visit(ArduinoApp arduinoApp);

}

