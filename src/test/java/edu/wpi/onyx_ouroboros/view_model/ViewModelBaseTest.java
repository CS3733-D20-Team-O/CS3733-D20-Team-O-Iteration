package edu.wpi.onyx_ouroboros.view_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageSwitchEvent;
import java.lang.reflect.Field;
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
   * New string for labeled elements in test ViewModel
   */
  private static final String TEST_STRING = "new string";
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
   * Tests the setNewText method in ViewModelBase
   *
   * @throws Exception for when reflection fails (it shouldn't unless a breaking change happens)
   */
  @Test
  public void testSetNewText() throws Exception {
    // Setup the test (setNewText is private so use reflection to invoke)
    val method = ViewModelBase.class
        .getDeclaredMethod("setNewText", Field.class, String.class);
    method.setAccessible(true);

    // Call setText on every field
    for (val field : viewModel.getClass().getDeclaredFields()) {
      val result = (boolean) method.invoke(viewModel, field, TEST_STRING);
      val fieldName = field.getName();
      if (fieldName.equals("keyNonLabel")) {
        // setNewText should have failed because there is no setText for this element
        assertFalse(result);
      } else {
        // setNewText should have succeeded because there is a setText for the other elements
        assertTrue(result);
      }
    }

    // Check to see that text changed
    assertEquals(TEST_STRING, viewModel.keyLabel.getText());
    assertEquals(TEST_STRING, viewModel.invalidKeyLabel.getText());
    assertEquals(TEST_STRING, viewModel.noKeyLabel.getText());
  }

  /**
   * Tests the onLanguageSwitch method in ViewModelBase
   */
  @Test
  public void testSwitchLanguage() {
    // Invoke the method
    viewModel.onLanguageSwitch(new LanguageSwitchEvent(ResourceBundle.getBundle("TestStrings")));

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
    private final Label keyLabel = new Label("");

    /**
     * Label that uses an invalid key
     */
    @Language(key = "invalidKey")
    private final Label invalidKeyLabel = new Label("");

    /**
     * Label that has no key
     */
    private final Label noKeyLabel = new Label("");

    /**
     * A non-Label that has a valid key
     */
    @SuppressWarnings("unused") // not used directly in code but is used via reflection
    @Language(key = "validKey")
    private final ProgressBar keyNonLabel = new ProgressBar();
  }
}
