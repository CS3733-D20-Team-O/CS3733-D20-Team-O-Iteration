package edu.wpi.onyx_ouroboros.model;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseUtilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

/**
 * Provides a convenient wrapper around Guice
 */
@Slf4j
public class Injector {

  /**
   * The instance of an injector to use for creating new objects
   */
  private static final com.google.inject.Injector injector = Guice
      .createInjector(new ProductionModule());

  /**
   * Creates an instance of a class via registered Modules
   *
   * @param tClass the Class of the object to create
   * @param <T>    the type of the object to create
   * @return a new object of the specified class
   */
  public static <T> T create(Class<T> tClass) {
    return injector.getInstance(tClass);
  }

  /**
   * Holds the necessary bindings for use in production
   */
  private static class ProductionModule extends AbstractModule {

    /**
     * Makes the necessary bindings for production
     */
    @Override
    protected void configure() {
      bind(EventBus.class).toInstance(EventBus.getDefault());
    }

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
}
