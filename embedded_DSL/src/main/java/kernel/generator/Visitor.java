package kernel.generator;

import kernel.ArduinoCode;
import kernel.logic.State;
import kernel.model.Actuator;
import kernel.model.Sensor;

public abstract class Visitor<T> {

    public abstract void visit(Actuator actuator);
    public abstract void visit(Sensor sensor);
    public abstract void visit(State state);
    public abstract void visit(ArduinoCode arduinoCode);
}

