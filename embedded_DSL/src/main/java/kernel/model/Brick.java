package kernel.model;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import lombok.Data;

@Data
public abstract class Brick implements Visitable {

    final String name;

    public abstract void accept(Visitor visitor);

    public abstract String declarationVarCode();

    public abstract String initCode();
}
