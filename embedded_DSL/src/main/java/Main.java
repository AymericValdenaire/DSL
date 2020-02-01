import static builder.ArduinoBuilder.analogicActuator;
import static builder.ArduinoBuilder.analogicSensor;
import static builder.ArduinoBuilder.arduino;
import static builder.ArduinoBuilder.digitalActuator;
import static builder.ArduinoBuilder.digitalSensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import kernel.ArduinoApp;
import kernel.generator.Generator;

public class Main {

  public static void main(String[] args) throws Exception {
    HashMap<String, Generator> arduinoAppGenerated = new HashMap<>();
    ArduinoApp arduinoApp;
    Generator generator;

    // ----------------
    // SCENARIO 1
    // ----------------

    arduinoApp =
        arduino("scenario1")
            .setup(digitalSensor("button", 10))
            .setup(digitalActuator("led", 11))
            .setup(digitalActuator("buzzer", 9))
              .states()
                .state("off")
                  .set("led").toLow()
                  .set("buzzer").toLow()
                  .when().ifIsEqual("button", "ON").thenGoToState("on")
                .state("on")
                  .set("led").toHigh()
                  .set("buzzer").toHigh()
                  .when().ifIsEqual("button", "OFF").thenGoToState("off")
              .initState("off")
          .build();

    generator = new Generator();
    arduinoApp.accept(generator);
    arduinoAppGenerated.put(arduinoApp.getName(), generator);

    // ----------------
    // BUILDER TEST
    // ----------------

    /*
    arduinoApp =
        arduino("scenario1")
            .setup(analogicSensor("button", 10))
            .setup(analogicActuator("led", 11))
            .setup(analogicActuator("buzzer", 9))
              .states()
                .state("on")
                  .set("test").toHigh()
                  .sleep(10)
                  .set("test").toLow()
                  .when().ifIsEqual("button", "1").and().ifIsEqual("button", "1").thenGoToState("off")
                .state("off")
                  .set("test").toLow()
                  .when().ifIsEqual("button", "1").thenGoToState("on")
              .initState("off")
            .build();

    generator = new Generator();
    arduinoApp.accept(generator);
    arduinoAppGenerated.put(arduinoApp.getName(), generator);*/


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
