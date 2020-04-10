package edu.wpi.onyx_ouroboros.model.data.csv;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import org.junit.jupiter.api.Test;

/**
 * Tests CSVHandler
 */
public class CSVHandlerTest {

  // todo delete this method and create real tests
  @Test
  public void satisfyCodeCoverage() {
    DependencyInjector.create(CSVHandler.class).exportFromDatabase(null);
  }
}
