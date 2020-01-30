package kernel.generator;

public interface Visitable {
  public void accept(Visitor visitor);
  public String initCode();
  public String declarationVarCode();
}
