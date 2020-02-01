package kernel.logic.statements.action;

import kernel.model.brick.actuator.Actuator;

public class Assignement extends Action {

    private Actuator var;
    private Object value;

    public Assignement(Actuator var, Object value){
        this.var = var;
        this.value = value;
    }

    @Override
    public String generateCode() {
        return String.format(var.generateAssignementCode(), value.toString());
    }
}
