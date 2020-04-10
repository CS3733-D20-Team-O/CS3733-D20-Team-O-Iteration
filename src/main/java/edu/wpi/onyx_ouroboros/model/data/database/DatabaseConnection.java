package edu.wpi.onyx_ouroboros.model.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * A Singleton for the database connection
 */
@Slf4j
public enum DatabaseConnection {
  /**
   * The single instance of the enum
   */
  INSTANCE;

  /**
   * The name of the database
   */
  private static final String DATABASE_NAME = "Odb";

  /**
   * The database connection
   */
  private final Connection connection;

  /**
   * Creates the database connection
   */
  DatabaseConnection() {
    // temp connection to initialize the actual *final* connection
    Connection temp = null;
    try {
      temp = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME + ";create=true");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    connection = temp;
  }

  /**
   * @return the database connection
   */
  public static Connection getConnection() {
    return INSTANCE.connection;
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
