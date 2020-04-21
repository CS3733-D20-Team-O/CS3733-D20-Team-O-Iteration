package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class NodeMapViewTest extends ApplicationTest {

  NodeMapView controller;
  Map<String, Node> nodeMap;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/NodeMapView.fxml"));
    // getClass().getResource("views/NodeMapView.fxml")
    StackPane root = loader.load();
    /*controller = loader.getController();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.show();
    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Node node5 = new Node("Node5", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node6 = new Node("Node6", 200, 200, 1, "Test", "Test", "Test", "Test");
    Map<String, Node> nodeMap = new HashMap<>();
    nodeMap.put(node1.getNodeID(), node1);
    nodeMap.put(node2.getNodeID(), node2);
    nodeMap.put(node3.getNodeID(), node3);
    nodeMap.put(node4.getNodeID(), node4);
    nodeMap.put(node5.getNodeID(), node5);
    nodeMap.put(node6.getNodeID(), node6);*/
  }

  @Test
  void testNodeMapFloorSelect() {
    controller.setNodeMap(nodeMap);
    clickOn("#btnIncrementFloor");
    clickOn("#btnIncrementFloor");
    clickOn("#btnIncrementFloor");
    clickOn("#btnIncrementFloor");
    clickOn("#btnIncrementFloor");
    clickOn("#btnDecrementFloor");
    clickOn("#btnDecrementFloor");
    clickOn("#btnDecrementFloor");
    clickOn("#btnDecrementFloor");
    clickOn("#btnDecrementFloor");
  }

  @Test
  void testNodeMapDrawNode() {
    controller.setNodeMap(nodeMap);
    controller.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node2"));
    controller.drawEdge(nodeMap.get("Node2"), nodeMap.get("Node3"));
    controller.drawEdge(nodeMap.get("Node3"), nodeMap.get("Node4"));
    controller.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node4"));
    controller.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node3"));
    controller.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
    clickOn("#btnIncrementFloor");
    controller.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
  }
  /*
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
  }*/
}