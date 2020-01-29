import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import kernel.ArduinoApp;
import kernel.generator.Generator;

import static builder.ArduinoBuilder.*;

public class Main {

    public static void main (String[] args) {
        ArduinoApp arduinoApp =
                arduino("monPremierCode")
                    .setup(sensor("led",2))
                    .setup(actuator("button",3))
                        .setup(lcd("lcd_0", 2))

                    .stateTable()
                        .state("on","led").when("button").isHigh().thenState("off","led")
                        .state("off","led").when("button").isHigh().thenState("on","led")
                    .endStateTable()
                .build();

        Generator generator = new Generator();
        arduinoApp.accept(generator);
        System.out.println(generator.getGeneratedCode());

        try {
            new File("out/").mkdir();

            PrintWriter writer = new PrintWriter("out/" + arduinoApp.getName() + ".ino", "UTF-8");
            writer.print(generator.getGeneratedCode());
            writer.close();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
