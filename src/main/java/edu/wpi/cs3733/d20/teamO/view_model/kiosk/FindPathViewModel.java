package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  @FXML
  private BorderPane root;
  @FXML
  private Pane clipper;
  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private AnchorPane sideBar;
  @FXML
  private Label floorLabel;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private NodeSelector startRoom, stopRoom;
  @FXML
  private JFXToggleButton handicap;

  private Map<String, Node> nodeMap, handicapMap;
  private final DatabaseWrapper database;
  private Node beginning, finish, defaultStart, defaultStop;

  private final SelectedPathFinder pathFinder;


  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    val clipRect = new Rectangle();
    clipRect.widthProperty().bind(clipper.widthProperty());
    clipRect.heightProperty().bind(clipper.heightProperty());
    clipper.setClip(clipRect);

    JFXDepthManager.setDepth(sideBar, 2);
    nodeMap = database.exportNodes();
    handicapMap = new HashMap<String, Node>();
    createHandicap();
    defaultStart = (Node) nodeMap.values().toArray()[0];
    defaultStop = (Node) nodeMap.values().toArray()[0];
    beginning = defaultStart;
    finish = defaultStop;

    startRoom.setOnNodeSelectedListener(node -> {
      nodeMapViewController.deleteNode(beginning);
      beginning = node;
      nodeMapViewController.draw();
      drawPath();
    });
    stopRoom.setOnNodeSelectedListener(node -> {
      nodeMapViewController.deleteNode(finish);
      finish = node;
      nodeMapViewController.draw();
      drawPath();
    });

    startRoom.setNodes(nodeMap.values());
    stopRoom.setNodes(nodeMap.values());
    resetPath();
  }

  /**
   * populate the handicap accessible map with nodes that a wheelchair can reach
   */
  public void createHandicap() {
    //create the map of nodes without edges
    for (Node node : nodeMap.values()) {
      if (!node.getNodeType().equals("STAI")) {
        Node newNode = new Node(node.getNodeID(), node.getXCoord(), node.getYCoord(),
            node.getFloor(), node.getBuilding(), node.getNodeType(),
            node.getLongName(), node.getShortName());
        handicapMap.put(newNode.getNodeID(), newNode);
      }
    }
    //populate only edges that still exists
    for (Edge edge : database.exportEdges()) {
      if (handicapMap.containsKey(edge.getStartID()) && handicapMap.containsKey(edge.getStopID())) {
        handicapMap.get(edge.getStartID()).getNeighbors().add(handicapMap.get(edge.getStopID()));
        handicapMap.get(edge.getStopID()).getNeighbors().add(handicapMap.get(edge.getStartID()));
      }
    }
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    nodeMapViewController.deleteNode(beginning);
    nodeMapViewController.deleteNode(finish);
    beginning = defaultStart;
    finish = defaultStop;
    stopRoom.clear();
    startRoom.clear();
    //TODO fix getting rid of the pop up
    nodeMapViewController.draw();
  }


  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    translateFloor();
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    translateFloor();
  }

  private void translateFloor() {
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawPath();
  }

  @FXML
  public void setZoom() {
    nodeMapViewController.zoom(zoomSlider.getValue() / 100);
  }

  @FXML
  public void zoomOutPressed() {
    zoomSlider.decrement();
    setZoom();
  }

  @FXML
  public void zoomInPressed() {
    zoomSlider.increment();
    setZoom();
  }

  private void drawPath() {
    List<Node> nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);
    nodeMapViewController.addNode(beginning);
    nodeMapViewController.addNode(finish);
    for (int i = 0; i < nodes.size() - 1; i++) {
      nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
    }
  }

  @FXML
  public void switchAccessibility() {
    if (handicap.isSelected()) {
      startRoom.setNodes(handicapMap.values());
      stopRoom.setNodes(handicapMap.values());
      System.out.println("HandicapMode");
    } else {
      startRoom.setNodes(nodeMap.values());
      stopRoom.setNodes(nodeMap.values());
    }
  }
}
