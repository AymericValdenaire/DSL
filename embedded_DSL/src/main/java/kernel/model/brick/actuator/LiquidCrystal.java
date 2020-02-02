package kernel.model.brick.actuator;

import lombok.Getter;

@Getter
public class LiquidCrystal extends Actuator{

  private final int[] matrixSize;
  private final int[] pins;

  public LiquidCrystal(String name, int pin, int[] matrixSize, int[] pins) {
    super(name, pin);
    this.matrixSize = matrixSize;
    this.pins = pins;
  }

  public String generateVarInitCode() {
    StringBuilder codeBuilder = new StringBuilder();
    for(Integer pin : pins) {
      codeBuilder.append(pin.toString()).append(",");
    }
    String code = codeBuilder.toString();
    return String.format("LiquidCrystal %s(%s);", name, code.substring(0, code.length() - 2));
  }

  public String generateSetupCode() {
    return String.format("\t\n%s.begin(%d, %d);", name, matrixSize[0], matrixSize[1]);
  }

  public String[] dependencies() {
    return new String[]{"LiquidCrystal"};
  }

  @Override
  public String generateAssignementCode(String value) {
    return "";
  }
}
