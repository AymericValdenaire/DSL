package builder;

import java.util.List;
import kernel.logic.State;
import kernel.model.brick.Brick;
import kernel.model.brick.sensor.Sensor;

@Deprecated
public class TransitionTableBuilder {

    private ArduinoBuilder parent;


    public TransitionTableBuilder(ArduinoBuilder parent){
        this.parent = parent;
    }

    public TransitionBuilder state(String state, String sensor){
        List<State> states = getStates();

        //on parcours notre machine a état
        for (State value : states) {
            // si l'état existe deja c a d que l'on a un nom d'etat et un sensor
            // identique alors on retourne un transitionbuilder
            if (value.getName().equals(state) && value.getSensor().getName().equals(sensor)) {
                //return new TransitionBuilder(this,value);
            }
        }
        //si l'etat n'existe pas on le défini et on l'ajoute a notre liste
        State new_State = new State();

        for (Brick brick : parent.getArduinoApp().getBricks()){
            if (brick.getName().equals(sensor)){
                new_State.setSensor((Sensor)brick);
                new_State.setName(state);
                getStates().add(new_State);
                //return new TransitionBuilder(this,new_State);
            }
        }
        throw new IllegalArgumentException("Illegal sensor name");
    }

    public ArduinoBuilder endStateTable(){
        return parent;
    }

    public List<State> getStates(){
        return parent.getArduinoApp().getStateMachine();
    }

    public List<Brick> getBricks(){
        return parent.getArduinoApp().getBricks();
    }

}
