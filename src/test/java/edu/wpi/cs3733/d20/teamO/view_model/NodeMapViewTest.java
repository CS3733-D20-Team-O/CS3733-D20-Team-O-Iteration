package edu.wpi.cs3733.d20.teamO.view_model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
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

  int result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, // testNodeMapFloorSelect
      result12, result13, result14, result15, result16, result17, // testNodeMapDrawNodeEdge
      result18, result19, result20, result21, result22, result23, result24, result25; // testNodeMapAddDeleteNode
  double result26, result27, result28, result29;

  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);

    stage
        .setScene(new Scene(loader.load(Main.class.getResourceAsStream("views/NodeMapView.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();

    Node node1 = new Node("Node1", 100, 100, 1, "Test", "Test", "Test", "Test");
    Node node2 = new Node("Node2", 100, 200, 1, "Test", "Test", "Test", "Test");
    Node node3 = new Node("Node3", 200, 100, 1, "Test", "Test", "Test", "Test");
    Node node4 = new Node("Node4", 200, 200, 1, "Test", "Test", "Test", "Test");
    Node node5 = new Node("Node5", 100, 100, 2, "Test", "Test", "Test", "Test");
    Node node6 = new Node("Node6", 200, 200, 2, "Test", "Test", "Test", "Test");
    Node node7 = new Node("Node7", 200, 200, 2, "Test", "Test", "Test", "Test");
    Map<String, Node> nodeMap = new HashMap<>();
    nodeMap.put(node1.getNodeID(), node1);
    nodeMap.put(node2.getNodeID(), node2);
    nodeMap.put(node3.getNodeID(), node3);
    nodeMap.put(node4.getNodeID(), node4);
    nodeMap.put(node5.getNodeID(), node5);
    nodeMap.put(node6.getNodeID(), node6);
    viewModel.setNodeMap(nodeMap);

    // testNodeMapFloorSelect
    result1 = viewModel.getNodeGroupList().size();
    viewModel.incrementFloor();
    result2 = viewModel.getNodeGroupList().size();
    viewModel.incrementFloor();
    result3 = viewModel.getNodeGroupList().size();
    viewModel.incrementFloor();
    result4 = viewModel.getNodeGroupList().size();
    viewModel.incrementFloor();
    result5 = viewModel.getNodeGroupList().size();
    viewModel.incrementFloor();
    result6 = viewModel.getNodeGroupList().size();
    viewModel.decrementFloor();
    result7 = viewModel.getNodeGroupList().size();
    viewModel.decrementFloor();
    result8 = viewModel.getNodeGroupList().size();
    viewModel.decrementFloor();
    result9 = viewModel.getNodeGroupList().size();
    viewModel.decrementFloor();
    result10 = viewModel.getNodeGroupList().size();
    viewModel.decrementFloor();
    result11 = viewModel.getNodeGroupList().size();

    // testNodeMapDrawNodeEdge
    result12 = viewModel.getEdgeGroupList().size(); // Expect 0
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node2"));
    viewModel.drawEdge(nodeMap.get("Node2"), nodeMap.get("Node3"));
    viewModel.drawEdge(nodeMap.get("Node3"), nodeMap.get("Node4"));
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node4"));
    viewModel.drawEdge(nodeMap.get("Node1"), nodeMap.get("Node3"));
    result13 = viewModel.getEdgeGroupList().size(); // Expect 5
    viewModel.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
    result14 = viewModel.getEdgeGroupList().size(); // Expect 5
    viewModel.incrementFloor();
    result15 = viewModel.getEdgeGroupList().size(); // Expect 0
    viewModel.drawEdge(nodeMap.get("Node5"), nodeMap.get("Node6"));
    result16 = viewModel.getEdgeGroupList().size(); // Expect 1
    viewModel.decrementFloor();
    result17 = viewModel.getEdgeGroupList().size(); // Expect 0

    // testNodeMapAddDeleteNode
    viewModel.deleteNode(node1);
    viewModel.deleteNode(node2);
    viewModel.deleteNode(node3);
    viewModel.deleteNode(node4);
    viewModel.deleteNode(node5);
    viewModel.deleteNode(node6);
    result18 = viewModel.getNodeGroupList().size(); // Expect 0
    viewModel.addNode(node1);
    viewModel.addNode(node2);
    viewModel.addNode(node3);
    viewModel.addNode(node4);
    viewModel.addNode(node5);
    viewModel.addNode(node6);
    result19 = viewModel.getNodeGroupList().size(); // Expect 4
    viewModel.incrementFloor();
    result20 = viewModel.getNodeGroupList().size(); // Expect 2
    viewModel.decrementFloor();
    viewModel.deleteNode(node5);
    result21 = viewModel.getNodeGroupList().size(); // Expect 4
    viewModel.deleteNode(node1);
    viewModel.deleteNode(node2);
    viewModel.deleteNode(node3);
    viewModel.deleteNode(node4);
    result22 = viewModel.getNodeGroupList().size(); // Expect 0
    viewModel.incrementFloor();
    result23 = viewModel.getNodeGroupList().size(); // Expect 1
    viewModel.deleteNode(node6);
    result24 = viewModel.getNodeGroupList().size(); // Expect 0
    viewModel.deleteNode(node7);
    result25 = viewModel.getNodeGroupList().size(); // Expect 0
    viewModel.decrementFloor();

    // testNodeRelocate
    Edge edge1 = new Edge("Edge1", "Node1", "Node2");
    viewModel.addNode(node1);
    viewModel.relocateNode(node1, 400, 450);
    result26 = viewModel.getNodeGroupList().get(0).getLayoutX(); // Expect 400.0
    result27 = viewModel.getNodeGroupList().get(0).getLayoutY(); // Expect 450.0

    // testNodeVisibility
    viewModel.makeNodeInvisible();
    result28 = viewModel.getNodeGroupList().get(0).getOpacity(); // Expect 0.0
    viewModel.makeNodeVisible();
    result29 = viewModel.getNodeGroupList().get(0).getOpacity(); // Expect 1.0
  }

  @Test
  void testNodeMapFloorSelect() {
    assertEquals(4, result1);
    assertEquals(2, result2);
    assertEquals(0, result3);
    assertEquals(0, result4);
    assertEquals(0, result5);
    assertEquals(0, result6);
    assertEquals(0, result7);
    assertEquals(0, result8);
    assertEquals(2, result9);
    assertEquals(4, result10);
    assertEquals(4, result11);
  }

  @Test
  void testNodeMapDrawNodeEdge() {
    assertEquals(0, result12);
    assertEquals(5, result13);
    assertEquals(5, result14);
    assertEquals(0, result15);
    assertEquals(1, result16);
    assertEquals(0, result17);
  }

  @Test
  void testNodeMapAddDeleteNode() {
    assertEquals(0, result18);
    assertEquals(4, result19);
    assertEquals(2, result20);
    assertEquals(4, result21);
    assertEquals(0, result22);
    assertEquals(1, result23);
    assertEquals(0, result24);
    assertEquals(0, result25);
  }

  @Test
  void testNodeRelocate() {
    assertEquals(400, result26);
    assertEquals(450, result27);
  }

  @Test
  void testNodeVisibility() {
    assertEquals(0, result28);
    assertEquals(1, result29);
  }
}