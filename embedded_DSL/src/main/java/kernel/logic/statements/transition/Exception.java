package kernel.logic.statements.transition;

import lombok.Getter;

@Getter
public class Exception{

  private int value;

  public Exception(int value){
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("error(%d)", value);
  }

}
