package edu.wpi.cs3733.d20.teamO.view_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

import edu.wpi.cs3733.d20.teamO.model.language.Language;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests ViewModelBase
 */
public class ViewModelBaseTest {

  /**
   * Starting text for labeled elements in test ViewModel
   */
  private static final String INITIAL_STRING = "initial";

  /**
   * The test view model
   */
  private ViewModelImpl viewModel;

  /**
   * Sets up TestFX
   *
   * @throws TimeoutException rethrow from registerPrimaryStage
   */
  @BeforeAll
  static void setupTestFX() throws TimeoutException {
    registerPrimaryStage();
  }

  /**
   * Resets each label to the INITIAL_STRING and creates a fresh ViewModelImpl
   */
  @BeforeEach
  public void setupTest() {
    viewModel = new ViewModelImpl();
    val labels = new Label[]{viewModel.keyLabel, viewModel.invalidKeyLabel, viewModel.noKeyLabel};
    for (val label : labels) {
      label.setText(INITIAL_STRING);
    }
  }

  /**
   * Tests the onLanguageSwitch method in ViewModelBase
   */
  @Test
  public void testSwitchLanguage() {
    // Invoke the method
    viewModel.switchToNewLocale(ResourceBundle.getBundle("TestStrings"));

    // Check every field to see if they are in the correct state
    assertEquals("This key is valid", viewModel.keyLabel.getText());
    assertEquals(INITIAL_STRING, viewModel.invalidKeyLabel.getText());
    assertEquals(INITIAL_STRING, viewModel.noKeyLabel.getText());
  }

  /**
   * Test ViewModel implementation
   */
  private static class ViewModelImpl extends ViewModelBase {

    /**
     * Label that uses a valid key
     */
    @Language(key = "validKey")
    private final Label keyLabel = new Label();

    /**
     * Label that uses an invalid key
     */
    @Language(key = "invalidKey")
    private final Label invalidKeyLabel = new Label();

    /**
     * Label that has no key
     */
    private final Label noKeyLabel = new Label();

    /**
     * A non-Label that has a valid key
     */
    @SuppressWarnings("unused") // not used directly in code but is used via reflection
    @Language(key = "validKey")
    private final ProgressBar keyNonLabel = new ProgressBar();
  }
}
