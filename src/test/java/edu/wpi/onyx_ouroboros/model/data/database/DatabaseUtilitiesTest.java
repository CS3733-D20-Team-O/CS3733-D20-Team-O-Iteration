package edu.wpi.onyx_ouroboros.model.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseConnection by checking to see if Apache Derby is correctly setup
 */
public class DatabaseUtilitiesTest {

  /**
   * Tests to see Apache Derby is correctly setup and functioning
   */
  @Test
  public void testSetupProperly() {
    assertTrue(DatabaseUtilities.checkForDerby());
  }

  /**
   * Tests the getURL method
   */
  @Test
  public void testGetURL() {
    assertEquals("jdbc:derby:myDB;create=true",
        DatabaseUtilities.getURL("myDB", false));
    assertEquals("jdbc:derby:memory:myDB;create=true",
        DatabaseUtilities.getURL("myDB", true));
  }
}
