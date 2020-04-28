package edu.wpi.cs3733.d20.teamO.view_model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/**
 * Tests NodeMapView
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class NodeMapViewTest extends FxRobot {

  @Mock
  EventBus eventBus;

  @InjectMocks
  NodeMapView viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);

    stage
        .setScene(new Scene(loader.load(Main.class.getResourceAsStream("views/NodeMapView.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  void testNodeMapFloorSelect() {
    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Node node5 = new Node("Node5", 100, 100, 2, "Test", "Test", "Test", "Test");
    Node node6 = new Node("Node6", 200, 200, 2, "Test", "Test", "Test", "Test");
    Map<String, Node> nodeMap = new HashMap<>();
    nodeMap.put(node1.getNodeID(), node1);
    nodeMap.put(node2.getNodeID(), node2);
    nodeMap.put(node3.getNodeID(), node3);
    nodeMap.put(node4.getNodeID(), node4);
    nodeMap.put(node5.getNodeID(), node5);
    nodeMap.put(node6.getNodeID(), node6);

    viewModel.setNodeMap(nodeMap);
    assertEquals(4, viewModel.getNodeGroupList().size());
    viewModel.incrementFloor();
    assertEquals(2, viewModel.getNodeGroupList().size());
    viewModel.incrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.incrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.incrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.incrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.decrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.decrementFloor();
    assertEquals(0, viewModel.getNodeGroupList().size());
    viewModel.decrementFloor();
    assertEquals(2, viewModel.getNodeGroupList().size());
    viewModel.decrementFloor();
    assertEquals(4, viewModel.getNodeGroupList().size());
    viewModel.decrementFloor();
    assertEquals(4, viewModel.getNodeGroupList().size());
  }

  @Test
  void testNodeMapDrawNodeEdge() {
    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Node node5 = new Node("Node5", 100, 100, 2, "Test", "Test", "Test", "Test");
    Node node6 = new Node("Node6", 200, 200, 2, "Test", "Test", "Test", "Test");
    Map<String, Node> nodeMap = new HashMap<>();
    nodeMap.put(node1.getNodeID(), node1);
    nodeMap.put(node2.getNodeID(), node2);
    nodeMap.put(node3.getNodeID(), node3);
    nodeMap.put(node4.getNodeID(), node4);
    nodeMap.put(node5.getNodeID(), node5);
    nodeMap.put(node6.getNodeID(), node6);
    viewModel.setNodeMap(nodeMap);
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node2"));
    viewModel.drawEdge(nodeMap.get("Node2"), nodeMap.get("Node3"));
    viewModel.drawEdge(nodeMap.get("Node3"), nodeMap.get("Node4"));
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node4"));
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node3"));
    viewModel.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
    viewModel.incrementFloor();
    viewModel.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
  }

  @Test
  void testNodeMapAddDeleteNode() {
    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Node node5 = new Node("Node5", 100, 100, 2, "Test", "Test", "Test", "Test");
    Node node6 = new Node("Node6", 200, 200, 2, "Test", "Test", "Test", "Test");
    Node node7 = new Node("Node6", 200, 200, 2, "Test", "Test", "Test", "Test");
    viewModel.addNode(node1);
    viewModel.addNode(node2);
    viewModel.addNode(node3);
    viewModel.addNode(node4);
    viewModel.addNode(node5);
    viewModel.addNode(node6);
    viewModel.incrementFloor();
    viewModel.decrementFloor();
    viewModel.deleteNode(node5);
    viewModel.deleteNode(node1);
    viewModel.deleteNode(node2);
    viewModel.deleteNode(node3);
    viewModel.deleteNode(node4);
    viewModel.incrementFloor();
    viewModel.deleteNode(node6);
    viewModel.deleteNode(node7);
  }
}