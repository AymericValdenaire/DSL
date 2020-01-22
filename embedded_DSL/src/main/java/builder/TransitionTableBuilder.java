package builder;

import kernel.logic.State;
import kernel.logic.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTableBuilder {

    private ArduinoBuilder parent;


    public TransitionTableBuilder(ArduinoBuilder parent){
        this.parent = parent;
    }

    public TransitionBuilder state(String state){
        List<State> states = getStates();
        //on parcours notre machine a état
        for (State value : states) {
            // si l'état existe deja alors on continue normalement
            if (value.getName().equals(state)) {
                return new TransitionBuilder(this,value);
            }
        }
        //si l'etat n'existe pas on le défini et on l'ajoute a notre liste
        State new_State = new State();
        new_State.setName(state);
        getStates().add(new_State);
        return new TransitionBuilder(this,new_State);
    }

    public ArduinoBuilder endStateTable(){
        return parent;
    }

    public List<State> getStates(){
        return parent.getArduinoApp().getStateMachine();
    }

}
