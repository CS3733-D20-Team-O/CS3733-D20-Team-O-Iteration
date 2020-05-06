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
import edu.wpi.cs3733.d20.teamO.model.network.WebApp.Step.Building;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp.Step.Instruction;
import edu.wpi.cs3733.d20.teamO.model.network.WebApp.Step.Instruction.Icon;
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
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
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
    val steps = generateSteps();
    try {
      // todo background color
      dialog.showFullscreen(new HBox(new ImageView(WebApp.createQRCode(null, steps))));
    } catch (Exception e) {
      log.error("Failed to create a QR code from the current path", e);
    }
  }

  /* Sample directions
  commonalities:
   - first direction on new map is head toward
    - after that we can use directions
   - same size as nodes array
   - end with you have arrived
   - divide nodes and instructions into steps at the SAME index (will always magically line up)

  starbucks
               - head toward hallway
  hallway
               - take __ right to elevator 1
  elevator 1
               - FLOOR SWITCH Go to the second floor
  elevator 2
               - Head toward __
  destination
               -- You have arrived.
   */

  private Instruction segmentToInstruction(int segment, String longName) {
    switch (segment) {
      case 0:
        return new Instruction("Take a hard right to " + longName, Icon.HARD_RIGHT);
      case 1:
        return new Instruction("Take a right to " + longName, Icon.RIGHT);
      case 2:
        return new Instruction("Take a slight right to " + longName, Icon.SLIGHT_RIGHT);
      case 3:
        return new Instruction("Go straight to " + longName, Icon.STRAIGHT);
      case 4:
        return new Instruction("Take a slight left to " + longName, Icon.SLIGHT_LEFT);
      case 5:
        return new Instruction("Take a left to " + longName, Icon.LEFT);
      default:
        return new Instruction("Take a hard left to " + longName, Icon.HARD_LEFT);
    }
  }

  private List<Instruction> createInstructionsFromNodes(List<Node> nodes) {
    val instructions = new ArrayList<Instruction>(nodes.size());
    for (int i = 0; i < nodes.size() - 1; ++i) {
      val prevNode = i > 0 ? nodes.get(i - 1) : null;
      val currNode = nodes.get(i);
      val nextNode = nodes.get(i + 1);
      if (i == 0 || !currNode.getFloor().equals(prevNode.getFloor()) ||
          !currNode.getBuilding().equals(prevNode.getBuilding())) {
        instructions.add(new Instruction(
            "Head toward " + nextNode.getLongName(), Icon.NAVIGATION));
      } else if (!currNode.getBuilding().equals(nextNode.getBuilding())) {
        // todo change this to just building once the building shit-show is sorted out
        //  ex: "Go to " + nextNode.getBuilding()
        instructions.add(new Instruction("Go to floor " + nextNode.getFloor()
            + " in " + nextNode.getBuilding(), Icon.NAVIGATION));
      } else if (!currNode.getFloor().equals(nextNode.getFloor())) {
        instructions.add(new Instruction(
            "Head to floor " + nextNode.getFloor(), Icon.NAVIGATION));
      } else {
        final double vertexX = currNode.getXCoord(), vertexY = currNode.getYCoord(),
            pi = Math.PI, seventh = 2 * pi / 7;
        val rawTheta = Math.atan2(nextNode.getYCoord() - vertexY, nextNode.getXCoord() - vertexX) -
            Math.atan2(prevNode.getYCoord() - vertexY, prevNode.getXCoord() - vertexX);
        val theta = rawTheta < 0 ? rawTheta + 2 * pi : rawTheta;
        for (double counter = 0; counter < 2 * pi; counter += seventh) {
          if (theta < counter) {
            instructions.add(segmentToInstruction((int) Math.round(counter / seventh),
                nextNode.getLongName()));
            break;
          }
        }
      }
    }
    instructions.add(new Instruction("You have arrived", Icon.DESTINATION));
    return instructions;
  }

  private List<Step> generateSteps() {
    val nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);

    // If there is no path, produce no steps
    if (nodes == null || nodes.isEmpty()) {
      return new ArrayList<>();
    }

    // Create instructions based on the nodes
    val instructions = createInstructionsFromNodes(nodes);

    // Transfer the instructions and nodes into steps
    val steps = new ArrayList<Step>();
    Building currBuilding = Building.from(nodes.get(0).getBuilding());
    String currFloor = nodes.get(0).getFloor();
    LinkedList<Node> nodeBuffer = new LinkedList<>();
    LinkedList<Instruction> instructionBuffer = new LinkedList<>();
    for (int i = 0; i < nodes.size(); ++i) {
      val currNode = nodes.get(i);
      if (currBuilding.equals(Building.from(currNode.getBuilding())) &&
          currFloor.equals(currNode.getFloor())) {
        // Add to the buffer
        nodeBuffer.add(currNode);
        instructionBuffer.add(instructions.get(i));
      } else {
        // Create a new step from the buffers
        steps.add(new Step(instructionBuffer, nodeBuffer, currBuilding, currFloor));

        // Reset the buffers and set them to use the new values
        currBuilding = Building.from(currNode.getBuilding());
        currFloor = currNode.getFloor();
        nodeBuffer = new LinkedList<>();
        nodeBuffer.add(currNode);
        instructionBuffer = new LinkedList<>();
        instructionBuffer.add(instructions.get(i));
      }
    }
    // Clear out remaining data in buffers into a step
    steps.add(new Step(instructionBuffer, nodeBuffer, currBuilding, currFloor));
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
