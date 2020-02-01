package kernel.logic.statements.action;

import kernel.generator.Visitor;

public class AssignementFromBrick extends Action {

    String var;
    String new_value;

    public AssignementFromBrick(String var, String new_value){
        this.var = var;
        this.new_value = new_value;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
