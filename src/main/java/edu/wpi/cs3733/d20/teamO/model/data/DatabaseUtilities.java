package edu.wpi.cs3733.d20.teamO.model.data;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Common utilities for databases
 */
@Slf4j
public class DatabaseUtilities {

  /**
   * Creates the database URL to get a connection with
   *
   * @param name     the name of the database
   * @param inMemory whether to store the database in memory or not (should be true for unit tests)
   * @return the URL of the embedded database
   */
  public static String getURL(String name, boolean inMemory) {
    return "jdbc:derby:" + (inMemory ? "memory:" : "") + name + ";create=true";
  }

  /**
   * Checks to see if Apache Derby is correctly setup
   *
   * @return true if Apache Derby is correctly setup, false otherwise
   */
  public static boolean checkForDerby() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      return true;
    } catch (ClassNotFoundException e) {
      val errorMsg = "Apache Derby Driver not found. Add the classpath to your module.\n" +
          "For IntelliJ do the following:\n" +
          "  File | Project Structure, Modules, Dependency tab\n" +
          "  Add by clicking on the green plus icon on the right of the window\n" +
          "  Select JARs or directories. Go to the folder where the database JAR is located\n" +
          "  Click OK, now you can compile your program and run it.";
      log.error(errorMsg, e);
      return false;
    }
  }
}
