package builder;

import kernel.logic.BoolExpression;
import kernel.logic.State;
import kernel.logic.Transition;

public class TransitionBuilder {

    private TransitionTableBuilder parent;
    private String state;
    private Transition transition;

    public TransitionBuilder(TransitionTableBuilder parent, String state){
        this.parent = parent;
        this.state = state;
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
        //transition.setDestination();
        return this.parent;
    }
}
