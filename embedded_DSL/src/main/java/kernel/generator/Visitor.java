package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.AbstractBoolExpression;
import kernel.logic.BoolExpression;
import kernel.logic.BoolOperator;
import kernel.logic.State;
import kernel.logic.statements.transition.Transition;
import kernel.model.brick.Brick;
import kernel.model.DigitalValue;

public abstract class Visitor<T> {

    public abstract void visit(Brick brick);
    public abstract void visit(State state);
    public abstract void visit(ArduinoApp arduinoApp);
    public abstract void visit(Transition transition);
    public abstract void visit(BoolExpression boolExpression);
    public abstract void visit(BoolOperator boolOperator);
    public abstract void visit(DigitalValue digitalValue);
    public abstract void visit(AbstractBoolExpression abstractBoolExpression);
}

