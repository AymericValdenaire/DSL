package kernel.logic.statements.action;

import kernel.model.brick.Brick;
import kernel.model.brick.Serial;
import kernel.model.brick.actuator.LiquidCrystal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Print extends Action{
  private String cmd;
  private  LiquidCrystal output;
  private final Serial serial;
  private  Brick from_brick;
  private final String msg;

  public Print(String cmd, LiquidCrystal output, Serial serial, Brick from_brick, String msg) {
    this.cmd = cmd;
    this.output = output;
    this.serial = serial;
    this.from_brick = from_brick;
    this.msg = msg;
  }

  public Print(String cmd,Serial serial, String msg){
    this.cmd = cmd;
    this.serial = serial;
    this.msg = msg;
  }
  @Override
  public String toString() {
    if(cmd.equals("PRINT")) {
      cmd = "print";
    } else {
      cmd = "println";
    }

    String payload = "";

    if(output != null) { //si ouput est un Lcd
      if(from_brick != null){
        payload = String.format("prettyDigitalPrint(\"%s\",%s)", from_brick.getName(), from_brick.toString());
      } else {
        payload = "\"" + msg.substring(0, output.getMatrixSize()[0]) + "\"";
      }
      return String.format("%s.setCursor(0, 0);\n"
          + "\t%s.%s(%s);\n"
          , output.getName(), output.getName(), cmd, payload);
    }

    if(serial != null) {
      if(from_brick != null) {
        payload = String.format("prettyDigitalPrint(\"%s\", %s)", from_brick.getName(), from_brick.toString());
      } else {
        payload = String.format("\"%s\"", msg);
      }
      return String.format("Serial.%s(%s);", cmd, payload);
    }

    return payload;
  }
}
