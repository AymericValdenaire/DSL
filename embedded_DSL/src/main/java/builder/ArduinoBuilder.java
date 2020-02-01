package builder;

import java.util.ArrayList;
import java.util.List;
import kernel.ArduinoApp;
import kernel.model.brick.Brick;
import kernel.model.brick.Lcd;
import kernel.model.brick.Serial;
import kernel.model.brick.actuator.Actuator;
import kernel.model.brick.actuator.AnalogicActuator;
import kernel.model.brick.actuator.DigitalActuator;
import kernel.model.brick.sensor.AnalogicSensor;
import kernel.model.brick.sensor.DigitalSensor;
import kernel.model.brick.sensor.Sensor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class ArduinoBuilder {

  private ArduinoApp arduinoApp;
  protected static List<Actuator> analogicActuators = new ArrayList<>();
  protected static List<Actuator> digitalActuators = new ArrayList<>();
  protected static List<Sensor> analogicSensors = new ArrayList<>();
  protected static List<Sensor> digitalSensors = new ArrayList<>();
  protected static List<Lcd> lcds = new ArrayList<>();
  protected static List<Serial> serials = new ArrayList<>();


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
    AnalogicSensor analogicSensor =  new AnalogicSensor(name, pin);
    analogicSensors.add(analogicSensor);
    return analogicSensor;
  }

  public static Brick digitalSensor(String name, int pin) {
    verifyArgument(name, pin);
    DigitalSensor digitalSensor = new DigitalSensor(name, pin);
    digitalSensors.add(digitalSensor);
    return digitalSensor;
  }

  public static Brick analogicActuator(String name, int pin) {
    verifyArgument(name, pin);
    AnalogicActuator analogicActuator = new AnalogicActuator(name, pin);
    analogicActuators.add(analogicActuator);
    return analogicActuator;
  }

  public static Brick digitalActuator(String name, int pin) {
    verifyArgument(name, pin);
    DigitalActuator digitalActuator = new DigitalActuator(name, pin);
    digitalActuators.add(digitalActuator);
    return digitalActuator;
  }

  public static Brick lcd(String name, int busId) {
    Lcd lcd = new Lcd(name, busId);
    lcds.add(lcd);
    return lcd;
  }

  public static Brick serial(String name, int baudrate) {
    Serial serial = new Serial(name, baudrate);
    serials.add(serial);
    return serial;
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
