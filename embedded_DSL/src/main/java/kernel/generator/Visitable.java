package kernel.generator;

public interface Visitable {
  public void accept(Visitor visitor);
}
