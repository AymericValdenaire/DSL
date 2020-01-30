package kernel.model;

import kernel.generator.Visitable;
import lombok.Data;

@Data
public abstract class Brick implements Visitable {

    final String name;

}
