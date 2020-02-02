package kernel.model.brick;

import lombok.Getter;

@Getter
public abstract class Brick{

    protected final String name;
    protected final int pin;

    public Brick(String name, int pin) {
        this.name = name;
        this.pin = pin;
    }

    public String generateSetupCode() {
        return String.format("def brick %s = %d;", name, pin);
    }

    @Override
    public String toString(){
        return String.format("\nint %s = %d;", name, pin);
    }

}
