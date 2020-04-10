package edu.wpi.onyx_ouroboros.model.data.database;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseConnection by checking to see if Apache Derby is correctly setup
 */
public class DatabaseConnectionTest {

  /**
   * Tests to see Apache Derby is correctly setup and functioning
   */
  @Test
  public void testDatabaseConnection() {
    assertTrue(DatabaseConnection.checkForDerby());
    assertNotEquals(null, DatabaseConnection.getConnection());
  }
}
