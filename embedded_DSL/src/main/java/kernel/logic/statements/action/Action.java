package kernel.logic.statements.action;


import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class Action implements Visitable {


    @Override
    public void accept(Visitor visitor) {
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
