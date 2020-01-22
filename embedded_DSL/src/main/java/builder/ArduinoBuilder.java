package builder;

import kernel.ArduinoApp;
import kernel.model.Actuator;
import kernel.model.Brick;
import kernel.model.Sensor;

public class ArduinoBuilder {

    ArduinoApp arduinoApp;

    public ArduinoBuilder(){

    }

    /**
     * On crée une instance du builder, et on lui ajoute une instance de l'app (point d'entrée
     * du code arduino)
     * @param name
     * @return
     */
    public static ArduinoBuilder arduino(String name){
        ArduinoBuilder arduinoBuilder = new ArduinoBuilder();
        arduinoBuilder.arduinoApp = new ArduinoApp();
        arduinoBuilder.arduinoApp.setName(name);
        return arduinoBuilder;
    }

    /**
     *
     * @return
     */
    public ArduinoApp build() {
        return arduinoApp; }

    public ArduinoBuilder setup(Brick brick){
        this.arduinoApp.getBricks().add(brick);
        return this;
    }

    public static Brick sensor(String name, int pin){

        verifyArgument(name, pin);
        Brick sensor = new Sensor();
        sensor.setName(name);
        sensor.setPin(pin);
        return sensor;
    }

    public static Brick actuator(String name, int pin){
        verifyArgument(name, pin);
        Brick actuator = new Actuator();
        actuator.setName(name);
        actuator.setPin(pin);
        return actuator;
    }

    private static void verifyArgument(String name, int pin){
        if(name.length() == 0 || !Character.isLowerCase(name.charAt(0)))
            throw new IllegalArgumentException("Illegal birck name");
        if(pin < 1 || pin > 12)
            throw new IllegalArgumentException("Illegal brick pin");
    }
}
