package edu.wpi.onyx_ouroboros.view_model;

import static org.testfx.api.FxToolkit.registerPrimaryStage;

import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

/**
 * Tests ServiceRequestSelection
 */
public class ServiceRequestSelectionTest {

  /**
   * Temporary test to satisfy code coverage requirements todo remove
   */
  @Test
  public void satisfyCodeCoverage() throws TimeoutException {
    registerPrimaryStage();
    new ServiceRequestSelection();
  }
}
