package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp.Step;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import edu.wpi.cs3733.d20.teamO.view_model.FloorSelector;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
  private JFXSlider zoomSlider;
  @FXML
  private NodeSelector startLocation, stopLocation;
  @FXML
  private JFXToggleButton handicap;
  @FXML
  private FloorSelector floorSelectorController;

  private Map<String, Node> nodeMap, handicapMap;
  private final DatabaseWrapper database;
  private Node beginning, finish, defaultStart, defaultStop;
  private final Dialog dialog;

  private final SelectedPathFinder pathFinder;


  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    handicapMap = new HashMap<String, Node>();

    val clipRect = new Rectangle();
    clipRect.widthProperty().bind(clipper.widthProperty());
    clipRect.heightProperty().bind(clipper.heightProperty());
    clipper.setClip(clipRect);

    JFXDepthManager.setDepth(sideBar, 2);
    nodeMap = database.exportNodes();
    fillHandicapMap();
    defaultStart = (Node) nodeMap.values().toArray()[0];
    defaultStop = (Node) nodeMap.values().toArray()[0];
    beginning = defaultStart;
    finish = defaultStop;

    startLocation.setOnNodeSelectedListener(node -> {
      nodeMapViewController.deleteNode(beginning);
      beginning = node;
      nodeMapViewController.draw();
      drawPath();
    });
    stopLocation.setOnNodeSelectedListener(node -> {
      nodeMapViewController.deleteNode(finish);
      finish = node;
      nodeMapViewController.draw();
      drawPath();
    });

    startLocation.setNodes(nodeMap.values());
    stopLocation.setNodes(nodeMap.values());
    resetPath();
  }

  /**
   * populate the handicap accessible map with nodes that a wheelchair can reach
   */
  private void fillHandicapMap() {
    //create the map of nodes without edges
    for (Node node : nodeMap.values()) {
      //if (node is not stair nor regular bath) or (node is bath and handicap)
      if ((!node.getNodeType().equals("STAI") && !node.getNodeType().equals("REST"))
          || (node.getNodeType().equals("REST") && node.getLongName().contains("Handicap"))) {
        val newNode = node.withNeighbors(new LinkedList<>());
        handicapMap.put(newNode.getNodeID(), newNode);
      }
    }
    //populate only edges that still exists
    for (Edge edge : database.exportEdges()) {
      if (handicapMap.containsKey(edge.getStartID()) && handicapMap.containsKey(edge.getStopID())) {
        val startNode = handicapMap.get(edge.getStartID());
        val stopNode = handicapMap.get(edge.getStopID());
        startNode.getNeighbors().add(stopNode);
        stopNode.getNeighbors().add(startNode);
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
    stopLocation.clear();
    startLocation.clear();
    nodeMapViewController.draw();
  }

  private void changeFloor() {
    nodeMapViewController.draw();
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
    resetPath();
    if (handicap.isSelected()) {
      startLocation.setNodes(handicapMap.values());
      stopLocation.setNodes(handicapMap.values());
    } else {
      startLocation.setNodes(nodeMap.values());
      stopLocation.setNodes(nodeMap.values());
    }
  }

  @FXML
  private void generateQR() {
    val steps = generateTextInstructions();
    steps.get(0).getNodes().addAll(database.exportNodes().values().stream()
        .filter(node -> node.getFloor() == 1 && node.getLongName().toLowerCase().contains("b"))
        .collect(Collectors.toList()));
    try {
      dialog.showFullscreen(new HBox(new ImageView(WebApp.createQRCode(steps))));
    } catch (Exception e) {
      System.out.println("exception happened for qr making");
    }

  }

  private ArrayList<Step> generateTextInstructions() {
    val steps = new ArrayList<Step>();
    List<Node> nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);
    List<Node> floor = new ArrayList<>();
    String stepMessage = "";
    for (int i = 0; i < nodes.size() - 1; i++) {
      if (i == 0) {
        floor.add(nodes.get(i));
      } else if (nodes.get(i).getFloor() == nodes.get(i - 1).getFloor()) {
        floor.add(nodes.get(i));
      } else {
        stepMessage = "";
        floor.clear();
        floor.add(nodes.get(i));
      }
      int x = nodes.get(i).getXCoord() - nodes.get(i + 1).getXCoord();
      int y = nodes.get(i).getYCoord() - nodes.get(i + 1).getYCoord();
      if (x < 0 && y == 0) {
        stepMessage = stepMessage.concat("Go West\n");
      } else if (x > 0 && y == 0) {
        stepMessage = stepMessage.concat("Go East\n");
      } else if (x == 0 && y < 0) {
        stepMessage = stepMessage.concat("Go South\n");
      } else if (x == 0 && y > 0) {
        stepMessage = stepMessage.concat("Go North\n");
      } else if (x > 0 && y < 0) {
        stepMessage = stepMessage.concat("Go South East\n");
      } else if (x < 0 && y > 0) {
        stepMessage = stepMessage.concat("Go North East\n");
      } else if (x > 0 && y > 0) {
        stepMessage = stepMessage.concat("Go North West\n");
      } else if (x < 0 && y < 0) {
        stepMessage = stepMessage.concat("Go South West\n");
      } else {
        stepMessage = stepMessage.concat("Go to Floor " + nodes.get(i + 1).getFloor());
        steps.add(new Step(stepMessage, floor, nodes.get(i).getFloor()));
      }

      if (i + 1 == nodes.size() - 1) {
        stepMessage = stepMessage.concat("You have arrived");
        steps.add(new Step(stepMessage, floor, nodes.get(i).getFloor()));
      }
    }

    return steps;
  }


  @FXML
  private void buttonSwitchFloor(int newFloor) {
    int currentFloor = nodeMapViewController.getFloor();
    while (currentFloor != newFloor) {
      if (currentFloor > newFloor) {
        nodeMapViewController.decrementFloor();
        currentFloor--;
      } else {
        nodeMapViewController.incrementFloor();
        currentFloor++;
      }
    }
    changeFloor();
  }

  @Override
  public void onEvent(Event event) {
    if (event.getClass().equals(FloorSwitchEvent.class)) {
      buttonSwitchFloor(((FloorSwitchEvent) event).getFloor());
    }
  }

}
