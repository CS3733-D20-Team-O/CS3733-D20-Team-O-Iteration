package edu.wpi.onyx_ouroboros.model.data.database;

import com.google.inject.AbstractModule;
import java.sql.Connection;

/**
 * Guice Module for database related classes
 */
public class DatabaseModule extends AbstractModule {

  /**
   * Makes necessary bindings in order to create a DatabaseWrapper
   */
  @Override
  protected void configure() {
    bind(Connection.class).toInstance(DatabaseConnection.getConnection());
    bind(DatabaseWrapper.class).to(DatabaseWrapperImpl.class);
  }
}
