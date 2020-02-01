package kernel.generator;

public interface Visitable {
  public void accept(Visitor visitor);
  public String generateSetupCode();
  public String generateCode();
}
