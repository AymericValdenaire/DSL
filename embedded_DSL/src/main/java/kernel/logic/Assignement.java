package kernel.logic;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

public class Assignement implements Visitable {

    String var;
    String new_value;

    public Assignement(String var, String new_value){
        this.var = var;
        this.new_value = new_value;
    }

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
