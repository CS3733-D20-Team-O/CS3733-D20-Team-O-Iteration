package edu.wpi.onyx_ouroboros.model;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import org.junit.jupiter.api.Test;

/**
 * Tests DependencyInjector
 */
public class DependencyInjectorTest {

  /**
   * Tests that DependencyInjector can create objects of different types
   */
  @Test
  public void checkObjectCreation() {
    assertNotEquals(null, DependencyInjector.create(CSVHandler.class));
    assertNotEquals(null, DependencyInjector.create(DatabaseWrapper.class));
  }
}
