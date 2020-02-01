package kernel.logic.statements.action;

import kernel.generator.Visitor;

public class Print extends Action{

    private String cmd;
    private String msg;

    public Print(String cmd, String msg){
        this.cmd = cmd;
        this.msg = msg;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
