package kernel.logic.statements.action;
import lombok.Getter;

@Getter
public class Wait extends Action{

  private int milli;

  public Wait(int milli) {
    this.milli = milli;
  }

  @Override
  public String toString() {
    return String.format("\n\tdelay(%d);", milli);
  }

}
