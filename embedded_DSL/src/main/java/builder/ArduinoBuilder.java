package builder;

import kernel.ArduinoApp;
import kernel.model.Actuator;
import kernel.model.Brick;
import kernel.model.Lcd;
import kernel.model.Sensor;

public class ArduinoBuilder {

    private ArduinoApp arduinoApp;

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


    public ArduinoApp build() {
        return arduinoApp;
    }

    public ArduinoBuilder setup(Brick brick){
        this.arduinoApp.getBricks().add(brick);
        return this;
    }

    public static Brick sensor(String name, int pin){

        verifyArgument(name, pin);
        return new Sensor(name, pin);
    }

    public static Brick actuator(String name, int pin){
        verifyArgument(name, pin);
        return new Actuator(name, pin);
    }

    public static Lcd lcd(String name, int busId){
        return new Lcd(name, busId);
    }

    // TODO Should not be here but in accept() method, check params on visit
    private static void verifyArgument(String name, int pin){
        if(name.length() == 0 || !Character.isLowerCase(name.charAt(0)))
            throw new IllegalArgumentException("Illegal brick name");
        if(pin < 1 || pin > 12)
            throw new IllegalArgumentException("Illegal brick pin");
    }

    public TransitionTableBuilder stateTable(){
        return new TransitionTableBuilder(this);
    }

    public ArduinoApp getArduinoApp() {
        return arduinoApp;
    }

}
