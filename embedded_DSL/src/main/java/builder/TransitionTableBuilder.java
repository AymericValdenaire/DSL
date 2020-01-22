package builder;

import kernel.logic.State;
import kernel.logic.Transition;

public class TransitionTableBuilder {

    private ArduinoBuilder parent;


    public TransitionTableBuilder(ArduinoBuilder parent){
        this.parent = parent;
    }

    public TransitionBuilder state(String state){
        return new TransitionBuilder(this,state);
    }

    public ArduinoBuilder endStateTable(){
        return parent;
    }


}
