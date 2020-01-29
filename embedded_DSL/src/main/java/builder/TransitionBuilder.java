package builder;

import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;
import kernel.model.Brick;
import kernel.model.Sensor;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class TransitionBuilder {

    private TransitionTableBuilder parent;
    private String state;
    private Transition transition;

    public TransitionBuilder(TransitionTableBuilder parent, State state){
        this.parent = parent;
        this.transition = new Transition();
        this.transition.setSource(state);
        state.getTransitions().add(this.transition);
    }

    public TransitionBuilder when(String sensor){
        transition.setSensor(sensor);
        return this;
    }

    public TransitionBuilder isHigh(){
        transition.setCondition("HIGH");
        return this;
    }

    public TransitionBuilder isLow(){
        transition.setCondition("LOW");
        return this;
    }

    public TransitionTableBuilder thenState(String state,String sensor){
        List<State> states = parent.getStates();
        //on parcours tous nos états pour voir si l'état déstination existe deja
        for (State value : states) {
            //si l'etat existe on le récupere et on le mets comme destination de la transition
            if (value.getName().equals(state) && value.getSensor().getName().equals(sensor)) {
                transition.setDestination(value);
                return this.parent;
            }
        }
        //si l'etat n'existe pas alors on le créer et on l'ajoute a notre liste d'état
        State new_State = new State();
        new_State.setName(state);
        for (Brick brick : parent.getBricks()){
            if (brick.getName().equals(sensor)){
                new_State.setSensor((Sensor)brick);
                new_State.setName(state);
                transition.setDestination(new_State);
                return this.parent;
            }
        }
        throw new IllegalArgumentException("Illegal sensor name");
    }
}
