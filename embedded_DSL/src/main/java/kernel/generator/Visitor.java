package kernel.generator;

import kernel.ArduinoCode;
import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;
import kernel.model.Actuator;
import kernel.model.Sensor;

public abstract class Visitor<T> {

    public abstract void visit(Actuator actuator);
    public abstract void visit(Sensor sensor);
    public abstract void visit(State state);
    public abstract void visit(ArduinoCode arduinoCode);
    public abstract void visit(Transition transition);
    public abstract void visit(BoolExpression boolExpression);
}

