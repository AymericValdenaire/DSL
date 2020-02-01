package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Getter;

@Getter
public abstract class Brick implements Visitable {

    private final String name;
    private final int pin;

    public Brick(String name, int pin) {
        this.name = name;
        this.pin = pin;
    }

    @Override
    public String initCode() {
        return String.format("def brick %s = %d;", name, pin);
    }

    @Override
    public String declarationVarCode(){
        return String.format("\nint %s = %d;", name, pin);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
