package kernel.model;

public class DigitalValue {

  private final String value;

  public DigitalValue(String value) {
    this.value = value;
  }

  public String generateSetupCode() {
    return "";
  }

  @Override
  public String toString() {
    return value;
  }
}
