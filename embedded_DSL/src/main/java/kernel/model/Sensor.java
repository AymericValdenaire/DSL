package kernel.model;

import kernel.generator.Visitor;

public class Sensor extends Brick {
    final int pin;
    final Boolean isAnalogic;

    public Sensor(String name, int pin, Boolean isAnalogic) {
        super(name);
        this.pin = pin;
        this.isAnalogic = isAnalogic;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String declarationVarCode() {
        return String.format("\tint %s = %s;\n", name, pin);
    }

    @Override
    public String initCode() {
        return String.format("\tpinMode(%s, INPUT);\n", this.pin);
    }

    public String generateReadCode() {
        if (isAnalogic) {
            return String.format("\tanalogRead(%s)\n", pin);
        } else {
            return String.format("\tdigitalRead(%s)\n", pin);
        }
    }

    public String generateLCDPrint(String lcdName) {
        if (isAnalogic) {
            return String.format("%1$s.setCursor(0, 0);\n"
                + "\t%1$s.print(prettyAnalogPrint(%2$s,%3$s));\n", lcdName, name, generateReadCode());
        } else {
            return String.format("%1$s.setCursor(0, 0);\n"
                + "\t%1$s.print(prettyDigitalPrint(%2$s,%3$s));\n", lcdName, name, generateReadCode());
        }
    }
}
