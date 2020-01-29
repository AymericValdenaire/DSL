package kernel.generator;

import kernel.ArduinoApp;
import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;
import kernel.model.Actuator;
import kernel.model.Brick;
import kernel.model.DigitalValue;
import kernel.model.Model;
import kernel.model.Sensor;
import kernel.model.Serial;

public abstract class Visitor<T> {

    public abstract void visit(Brick brick);
    public abstract void visit(State state);
    public abstract void visit(ArduinoApp arduinoApp);
    public abstract void visit(Transition transition);
    public abstract void visit(BoolExpression boolExpression);
    public abstract void visit(Serial serial);
    public abstract void visit(DigitalValue digitalValue);
    public abstract void visit(Model model);
}

