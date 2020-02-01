package kernel.model.brick;

public class Serial extends Brick {

    private final String name;
    private final int baudrate;

    public Serial(String name, int baudrate) {
        super(name, -1);
        this.name = name;
        this.baudrate = baudrate;
    }

    @Override
    public String generateSetupCode() {
        return String.format("\tSerial.begin(%s);\n", this.baudrate);
    }

    @Override
    public String toString() {
        return "Serial.readString()";
    }

}
