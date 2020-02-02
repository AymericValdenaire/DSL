package kernel.logic;

import java.util.List;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.logic.statements.Statement;
import lombok.Getter;

@Getter
public class State implements Visitable {

  private final String name;
  private final List<Statement> statements;
  private final Float frequency;
  private final Integer maxStateSleep;

  public State(String name, List<Statement> statements, Float frequency){
    this.name = name;
    this.statements = statements;
    this.frequency = frequency;
    if (frequency > 0) {
      Float maxStateSleepFloat = (1000 * (1 / frequency));
      this.maxStateSleep = maxStateSleepFloat.intValue();
    } else {
      this.maxStateSleep = null;
    }
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    String state = String.format("void {%s}()\n"
        + "{{\n"
        + "{%s} // No transition, loop on {%s} state"
        + "\t{%s}();\n"
        + "}}");

    Boolean noTransition = true;
    
  }

  @Override
  public String generateSetupCode() {
    return "";
  }
}
