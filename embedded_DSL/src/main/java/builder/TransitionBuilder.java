package builder;

import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionBuilder {

    private TransitionTableBuilder parent;
    private String state;
    private Transition transition;

    public TransitionBuilder(TransitionTableBuilder parent, State state){
        this.parent = parent;
        this.transition = new Transition();
        state.getTransitions().add(this.transition);
    }

    public TransitionBuilder when(String sensor){
        transition.setSensor(sensor);
        return this;
    }

    public TransitionBuilder isHigh(){
        BoolExpression cond = new BoolExpression();
        cond.setExpression("HIGH");
        transition.setCondition(cond);
        return this;
    }

    public TransitionBuilder isLow(){
        BoolExpression cond = new BoolExpression();
        cond.setExpression("LOW");
        transition.setCondition(cond);
        return this;
    }

    public TransitionTableBuilder thenState(String state){
        List<State> states = parent.getStates();
        //on parcours tous nos états pour voir si l'état déstination existe deja
        for (State value : states) {
            //si l'etat existe on le récupere et on le mets comme destination de la transition
            if (value.getName().equals(state)) {
                transition.setDestination(value);
                return this.parent;
            }
        }
        //si l'etat n'existe pas alors on le créer et on l'ajoute a notre liste d'état
        State new_State = new State();
        new_State.setName(state);
        transition.setDestination(new_State);
        return parent;
    }
}
