package edu.wpi.cs3733.d20.teamO.injection_modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseUtilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Module used for accessing the database
 */
@Slf4j
public class DatabaseModule extends AbstractModule {

  /**
   * Provide a single connection for database usage
   */
  @Provides
  @Singleton
  public Connection provideConnection() throws SQLException {
    log.debug("Creating an embedded database connection");
    val url = DatabaseUtilities.getURL("Odb", false);
    return DriverManager.getConnection(url);
  }
}