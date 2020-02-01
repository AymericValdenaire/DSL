package builder;

import kernel.ArduinoApp;
import kernel.model.brick.Brick;
import kernel.model.brick.Lcd;
import kernel.model.brick.Serial;
import kernel.model.brick.actuator.AnalogicActuator;
import kernel.model.brick.actuator.DigitalActuator;
import kernel.model.brick.sensor.AnalogicSensor;
import kernel.model.brick.sensor.DigitalSensor;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ArduinoBuilder {

  private ArduinoApp arduinoApp;

  public ArduinoBuilder() {

  }

  /**
   * On crée une instance du builder, et on lui ajoute une instance de l'app (point d'entrée du code
   * arduino)
   */
  public static ArduinoBuilder arduino(String name) {
    ArduinoBuilder arduinoBuilder = new ArduinoBuilder();
    arduinoBuilder.arduinoApp = new ArduinoApp();
    arduinoBuilder.arduinoApp.setName(name);
    return arduinoBuilder;
  }

  public ArduinoApp build() {
    return arduinoApp;
  }

  public ArduinoBuilder setup(Brick brick) {
    this.arduinoApp.getBricks().add(brick);
    return this;
  }

  public static Brick analogicSensor(String name, int pin) {
    verifyArgument(name, pin);
    return new AnalogicSensor(name, pin);
  }

  public static Brick digitalSensor(String name, int pin) {
    verifyArgument(name, pin);
    return new DigitalSensor(name, pin);
  }

  public static Brick analogicActuator(String name, int pin) {
    verifyArgument(name, pin);
    return new AnalogicActuator(name, pin);
  }

  public static Brick digitalActuator(String name, int pin) {
    verifyArgument(name, pin);
    return new DigitalActuator(name, pin);
  }

  public static Brick lcd(String name, int busId) {
    return new Lcd(name, busId);
  }

  public static Brick serial(String name, int baudrate) {
    return new Serial(name, baudrate);
  }

  // TODO Should not be here but in accept() method, check params on visit
  private static void verifyArgument(String name, int pin) {
      if (name.length() == 0 || !Character.isLowerCase(name.charAt(0))) {
          throw new IllegalArgumentException("Illegal brick name");
      }
      if (pin < 1 || pin > 12) {
          throw new IllegalArgumentException("Illegal brick pin");
      }
  }

  public TransitionTableBuilder stateTable() {
    return new TransitionTableBuilder(this);
  }

  public StatesBuilder states() {
    return new StatesBuilder(this);
  }

  protected ArduinoApp getArduinoApp() {
    return arduinoApp;
  }

}
