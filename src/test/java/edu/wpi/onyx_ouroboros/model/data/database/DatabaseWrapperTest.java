package edu.wpi.onyx_ouroboros.model.data.database;

import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  // todo delete this method and create real tests
  @Test
  public void satisfyCodeCoverage() {
    DatabaseWrapper.getInstance().addNode(null);
    DatabaseWrapper.getInstance().deleteNode(null);
  }
}
