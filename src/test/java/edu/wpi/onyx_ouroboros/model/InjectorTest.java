package edu.wpi.onyx_ouroboros.model;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.wpi.onyx_ouroboros.model.astar.AStar;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import org.junit.jupiter.api.Test;

/**
 * Tests Injector
 */
public class InjectorTest {

  /**
   * Tests that Injector can create objects of different types
   */
  @Test
  public void checkObjectCreation() {
    assertNotEquals(null, Injector.create(CSVHandler.class));
    assertNotEquals(null, Injector.create(DatabaseWrapper.class));
    assertNotEquals(null, Injector.create(AStar.class));
  }
}
