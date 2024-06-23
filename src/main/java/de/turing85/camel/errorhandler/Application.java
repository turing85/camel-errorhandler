package de.turing85.camel.errorhandler;

import org.apache.camel.main.Main;
import org.apache.camel.main.MainConfigurationProperties;

public class Application {
  public static void main(String... args) throws Exception {
    Main main = new Main();
    try (MainConfigurationProperties configure = main.configure()) {
      configure.addRoutesBuilder(new MyDirectRoute());
    }
    main.run();
  }
}
