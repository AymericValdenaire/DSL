package kernel.model.brick;

import kernel.generator.Visitor;

public class Lcd extends Brick {
    private final int bus_id;

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
        super(name, busId);
        this.bus_id = busId;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String generateCode() {
        return String.format("LiquidCrystal lcd(%s);\n", getBusPin());
    }

    @Override
    public String generateSetupCode() {
        return String.format("\t%s.begin(16, 2);\n", name);
    }
}
