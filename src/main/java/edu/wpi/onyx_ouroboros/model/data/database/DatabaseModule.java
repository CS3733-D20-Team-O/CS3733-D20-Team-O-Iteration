package edu.wpi.onyx_ouroboros.model.data.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Singleton;
import lombok.val;

/**
 * Guice Module for database related classes
 */
public class DatabaseModule extends AbstractModule {

  /**
   * Makes necessary bindings in order to create a DatabaseWrapper
   */
  @Override
  @SuppressWarnings("all") // Silences uninstantiable warning
  protected void configure() {
    bind(DatabaseWrapper.class).to(DatabaseWrapperImpl.class);
  }

  /**
   * Provide a single connection for database usage
   */
  @Provides
  @Singleton
  public Connection provideConnection() throws SQLException {
    val url = DatabaseUtilities.getURL("Odb", false);
    return DriverManager.getConnection(url);
  }
}
