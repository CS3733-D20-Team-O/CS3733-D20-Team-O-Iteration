package edu.wpi.onyx_ouroboros.view_model;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.onyx_ouroboros.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

/*
 * Tests the connection between the fxml and the Database CSV model
 */
public class DatabaseCSVConnectionTest extends ApplicationTest {

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/prototype/DatabaseCSVConnection.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  void testButtonEnable() {
    clickOn("#txtfieldFileLocation");
    write("1234");
    verifyThat("#btnImport", NodeMatchers.isEnabled());
    verifyThat("#btnExport", NodeMatchers.isEnabled());
  }

  @Test
  void testButtonDisable() {
    clickOn("#txtfieldFileLocation");
    write("1234");
    push(KeyCode.BACK_SPACE);
    push(KeyCode.BACK_SPACE);
    push(KeyCode.BACK_SPACE);
    push(KeyCode.BACK_SPACE); // Clear the text box (quite inelegant, but couldn't find the clear method for testfx)

    verifyThat("#btnImport", NodeMatchers.isDisabled());
    verifyThat("#btnExport", NodeMatchers.isDisabled());
  }

  @Test
  void testImportButton() {
    clickOn("#txtfieldFileLocation");
    write("Test");
    clickOn("#btnImport");
  }

  @Test
  void testExportButton() {
    clickOn("#txtfieldFileLocation");
    write("Test");
    clickOn("#btnExport");
  }
}