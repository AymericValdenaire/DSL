package kernel.logic.statements.action;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class Assignement extends Action {

    private String var;
    private String new_value;

    public Assignement(String var, String new_value){
        this.var = var;
        this.new_value = new_value;
    }

    @Override
    public void accept(Visitor visitor) {
            visitor.visit(this);
    }

    @Override
    public String initCode() {
        return null;
    }

    @Override
    public String declarationVarCode() {
        return null;
    }
}
