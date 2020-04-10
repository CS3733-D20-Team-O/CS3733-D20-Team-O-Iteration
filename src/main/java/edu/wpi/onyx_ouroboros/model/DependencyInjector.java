package edu.wpi.onyx_ouroboros.model;

import com.google.inject.Guice;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVModule;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseModule;
import lombok.val;

/**
 * Provides a convenient wrapper around Guice
 */
public class DependencyInjector {

  /**
   * Creates an instance of a class via registered Modules
   *
   * @param tClass the Class of the object to create
   * @param <T>    the type of the object to create
   * @return a new object of the specified class
   */
  public static <T> T create(Class<T> tClass) {
    val injector = Guice.createInjector(
        new DatabaseModule(),
        new CSVModule()
    );
    return injector.getInstance(tClass);
  }
}
