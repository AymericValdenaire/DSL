package kernel.model;

import kernel.generator.Visitor;

public class Lcd  extends Brick {
    final int bus_id;
    private String pins;

    private String getBusPin() {
        switch (this.bus_id){
            case 1 :
                return "2, 3, 4, 5, 6, 7, 8";
            case 2 :
                return "10, 11, 12, 13, 14, 15, 16";
            default:
                return null;
        }
    }

    public Lcd(String name, int busId){
        super(name);
        this.bus_id = busId;
        try {
            this.pins = getBusPin();
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        if (this.pins == null){
            throw new IllegalArgumentException(String.format("Invalid Bus for lcd \"%s\" must be 1 or 2", this.name));
        }
        visitor.visit(this);
    }

    @Override
    public String declarationVarCode() {
        return String.format("LiquidCrystal lcd(%s);\n", this.pins);
    }

    @Override
    public String initCode() {
        return String.format("\t%s.begin(16, 2);\n",this.name);
    }
}
