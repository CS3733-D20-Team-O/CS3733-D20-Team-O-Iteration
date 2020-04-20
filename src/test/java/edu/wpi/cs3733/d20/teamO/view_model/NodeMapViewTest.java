package edu.wpi.cs3733.d20.teamO.view_model;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

public class NodeMapViewTest extends ApplicationTest {

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/NodeMapView.fxml"));
    //Main.class.getResource("views/prototype/DatabaseCSVConnection.fxml")
    Parent root = loader.load();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  void testNodeMap() {
    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Map<String, Node> nodeMap = new HashMap<>();
    nodeMap.put(node1.toString(), node1);

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
    push(
        KeyCode.BACK_SPACE); // Clear the text box (quite inelegant, but couldn't find the clear method for testfx)

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