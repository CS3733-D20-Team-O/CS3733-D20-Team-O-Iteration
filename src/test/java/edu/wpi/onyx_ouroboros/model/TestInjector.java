package edu.wpi.onyx_ouroboros.model;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseUtilities;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapperTestImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import lombok.val;

/**
 * Used to inject dependencies for unit tests
 */
public class TestInjector {

  /**
   * Creates an instance of a class via registered Modules
   *
   * @param tClass the Class of the object to create
   * @param <T>    the type of the object to create
   * @return a new object of the specified class
   */
  public static <T> T create(Class<T> tClass) {
    val injector = Guice.createInjector(new TestModule());
    return injector.getInstance(tClass);
  }

  /**
   * Holds the necessary bindings for testing
   */
  private static class TestModule extends AbstractModule {

    /**
     * Makes the necessary bindings for testing
     */
    @Override
    protected void configure() {
      bind(DatabaseWrapper.class).to(DatabaseWrapperTestImpl.class);
    }

    /**
     * Provide a unique in-memory connection for each database usage
     */
    @Provides
    public Connection provideConnection() throws SQLException {
      val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      val length = 10;
      val randomName = new StringBuilder(length);
      val rand = new Random();
      for (int i = 0; i < length; ++i) {
        val index = rand.nextInt(chars.length());
        randomName.append(chars.charAt(index));
      }
      val url = DatabaseUtilities.getURL(randomName.toString(), true);
      return DriverManager.getConnection(url);
    }
  }
}
