import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kernel.ArduinoApp;
import kernel.generator.Generator;

import static builder.ArduinoBuilder.*;

public class Main {

    public static void main (String[] args) {
        HashMap<String, Generator> arduinoAppGenerated = new HashMap<>();
        ArduinoApp arduinoApp;
        Generator generator;

        // ----------------
        // SCENARIO 1
        // ----------------

        /*
        SENSOR button 10
        ACTUATOR led 11
        ACTUATOR buzzer 9

        INIT off {
            SET led OFF
            SET buzzer OFF
            button == ON -> on
        }

        on {
            SET led ON
            SET buzzer ON
            button == OFF -> off
        }
         */

        // Todo : manque les SET dans les states
        arduinoApp =
                arduino("scenario1")
                    .setup(sensor("button", 10))
                    .setup(actuator("led", 11))
                    .setup(actuator("buzzer", 9))

                    .states()
                        .state("on")
                            .set("test").toHigh()
                            .set("test").toLow()
                            .when().iff(true).thenSet("test").toLow()
                        .state("off")
                            .set("test").toLow()
                            .when().iff(true).thenSet("blbl").toHigh()
                    .build();

        generator = new Generator();
        arduinoApp.accept(generator);
        arduinoAppGenerated.put(arduinoApp.getName(), generator);

        // ----------------
        // TEST CODE2
        // ----------------

        arduinoApp =
            arduino("monPremierCode2")
                .setup(sensor("led",2))
                .setup(actuator("button",3))
                .setup(lcd("lcd_0", 2))

                .stateTable()
                .state("on","led").when("button").isHigh().thenState("off","led")
                .state("off","led").when("button").isHigh().thenState("on","led")
                .endStateTable()
                .build();

        generator = new Generator();
        arduinoApp.accept(generator);
        arduinoAppGenerated.put(arduinoApp.getName(), generator);


        try {
            new File("out/").mkdir();

            for (Map.Entry<String, Generator> entry : arduinoAppGenerated.entrySet()) {
                PrintWriter writer = new PrintWriter("out/" + entry.getKey() + ".ino", "UTF-8");
                writer.print(entry.getValue().getGeneratedCode());
                writer.close();
            }

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
