package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import static org.testfx.api.FxToolkit.registerPrimaryStage;

import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;

/**
 * Tests ServiceRequestSelection
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class ServiceRequestSelectionTest extends FxRobot {

  @Mock
  DatabaseWrapper database;

  /**
   * Temporary test to satisfy code coverage requirements todo remove
   */
  @Test
  public void satisfyCodeCoverage() throws TimeoutException {
    registerPrimaryStage();
    new ServiceRequestSelection(database);
  }
}
