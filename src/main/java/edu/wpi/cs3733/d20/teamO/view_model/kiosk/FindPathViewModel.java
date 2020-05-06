package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp.Step;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
  private Label floorLabel;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private NodeSelector startLocation, stopLocation;
  @FXML
  private JFXToggleButton handicap;

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
    handicapMap = new HashMap<String, Node>();
    createHandicap();
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
  public void createHandicap() {
    //create the map of nodes without edges
    for (Node node : nodeMap.values()) {
      if (!node.getNodeType().equals("STAI")) {
        val newNode = node.withNeighbors(new LinkedList<>());
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
    stopLocation.clear();
    startLocation.clear();
    nodeMapViewController.draw();
  }


  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    changeFloor();
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    changeFloor();
  }

  private void changeFloor() {
    nodeMapViewController.draw();
    floorLabel.setText(" " + nodeMapViewController.getFloor());
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
      System.out.println("HandicapMode");
    } else {
      startLocation.setNodes(nodeMap.values());
      stopLocation.setNodes(nodeMap.values());
    }
  }

  @FXML
  private void generateQR() {
    val steps = new ArrayList<Step>();
    try {
      dialog
          .showFullscreen(new HBox(new ImageView(WebApp.createQRCode(null, steps)))); // todo color
    } catch (Exception e) {
      System.out.println("exception happened for qr making");
    }

  }

//  private ArrayList<Step> generateTextInstructions() {
//    val steps = new ArrayList<Step>();
//    List<Node> nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);
//    for (int i = 0; i < nodes.size() - 1; i++) {
//      int x = nodes.get(i).getXCoord() - nodes.get(i + 1).getXCoord();
//      int y = nodes.get(i).getYCoord() - nodes.get(i + 1).getYCoord();
//      if (x < 0 && y == 0) {
//        steps.add(new Step("Go West", nodes, nodes.get(i).getFloor()));
//      } else if (x > 0 && y == 0) {
//        steps.add(new Step("Go East", nodes, nodes.get(i).getFloor()));
//      } else if (x == 0 && y < 0) {
//        steps.add(new Step("Go South", nodes, nodes.get(i).getFloor()));
//      } else if (x == 0 && y > 0) {
//        steps.add(new Step("Go North", nodes, nodes.get(i).getFloor()));
//      } else if (x > 0 && y < 0) {
//        steps.add(new Step("Go South East", nodes, nodes.get(i).getFloor()));
//      } else if (x < 0 && y > 0) {
//        steps.add(new Step("Go North East", nodes, nodes.get(i).getFloor()));
//      } else if (x > 0 && y > 0) {
//        steps.add(new Step("Go North West", nodes, nodes.get(i).getFloor()));
//      } else if (x < 0 && y < 0) {
//        steps.add(new Step("Go South West", nodes, nodes.get(i).getFloor()));
//      } else {
//        steps.add(
//            new Step("Go to Floor " + nodes.get(i + 1).getFloor(), nodes, nodes.get(i).getFloor()));
//      }
//
//      if (i + 1 == nodes.size() - 1) {
//        steps.add(new Step("You have arrived ", new ArrayList<>(), nodes.get(i).getFloor()));
//      }
//    }
//
//    return steps;
//  }


  @FXML
  private void buttonSwitchFloor(ActionEvent e) {
    int newFloor = Integer.parseInt(((JFXButton) e.getSource()).getText());
    int currentFloor = Integer.parseInt(floorLabel.getText().stripLeading());
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
}
