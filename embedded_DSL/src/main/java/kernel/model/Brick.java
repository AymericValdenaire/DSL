package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;

@Data
public abstract class Brick implements Visitable {

    private int pin;
    private String name;

    public abstract void accept(Visitor visitor);
}
