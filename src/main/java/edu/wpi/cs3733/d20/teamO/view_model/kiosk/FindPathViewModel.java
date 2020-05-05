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
  public void generateQR(ActionEvent actionEvent) {
    val steps = generateTextInstructions();
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
    StringBuilder stepMessage = new StringBuilder();
    int currentFloor = -1;
    for (int i = 0; i < nodes.size() - 1; i++) {
      val current = nodes.get(i);
      val next = nodes.get(i + 1);
      if (i == 0 || current.getFloor() == currentFloor) {
        // add this node along with any directions to the buffers
        floor.add(current);
        int x = nodes.get(i).getXCoord() - nodes.get(i + 1).getXCoord();
        int y = nodes.get(i).getYCoord() - nodes.get(i + 1).getYCoord();
        if (x < 0 && y == 0) {
          stepMessage.append("Go Left\n");
        } else if (x > 0 && y == 0) {
          stepMessage.append("Go Right\n");
        } else if (x == 0 && y < 0) {
          stepMessage.append("Go Down\n");
        } else if (x == 0 && y > 0) {
          stepMessage.append("Go Up\n");
        } else if (x > 0 && y < 0) {
          stepMessage.append(" ");
          //stepMessage = stepMessage.concat("Go South East\n");
        } else if (x < 0 && y > 0) {
          stepMessage.append(" ");
          //stepMessage = stepMessage.concat("Go North East\n");
        } else if (x > 0 && y > 0) {
          stepMessage.append(" ");
          //stepMessage = stepMessage.concat("Go North West\n");
        } else if (x < 0 && y < 0) {
          stepMessage.append(" ");
          //stepMessage = stepMessage.concat("Go South West\n");
        } else {
          stepMessage.append("Go to Floor ");
          stepMessage.append(nodes.get(i + 1).getFloor());
        }
      } else {
        // take all the buffers and put them into a step
        // set floor and reset buffers (nodeBuffer and instructionBuilder) to work with new current node
        steps.add(new Step(stepMessage.toString(), floor, currentFloor));
        currentFloor = current.getFloor();
        stepMessage = new StringBuilder();
      }
      if (i + 1 == nodes.size() - 1) {
        stepMessage.append("You have arrived");
        steps.add(new Step(stepMessage.toString(), floor, currentFloor));
      }
    }
    return steps;
  }


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
