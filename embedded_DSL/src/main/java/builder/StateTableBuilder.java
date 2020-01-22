package builder;

import kernel.logic.State;
import kernel.logic.Transition;

public class StateTableBuilder {

    private ArduinoBuilder parent;

    public StateTableBuilder(ArduinoBuilder parent){
        this.parent = parent;
    }

    public ArduinoBuilder endStateTable(){
        return parent;
    }


}
