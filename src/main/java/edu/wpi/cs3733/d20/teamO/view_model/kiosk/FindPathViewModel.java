package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import static com.jfoenix.controls.JFXButton.ButtonType.RAISED;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.RedrawEvent;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  private enum AccessabilityState {
    NONE, HANDICAP, STAIR
  }

  private AccessabilityState currentState;

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
  private JFXButton handicap, stairsOnly;
  @FXML
  private JFXColorPicker colorPicker;
  @FXML
  private HBox mapSwitcherButtons;

  private Map<String, Node> nodeMap, handicapMap, stairsMap;
  private final DatabaseWrapper database;
  private Node beginning, finish, defaultStart, defaultStop;
  private final Dialog dialog;
  private final SnackBar snackBar;
  private Color color = Color.web("fd8842");

  private final SelectedPathFinder pathFinder;

  @Override
  public void onEvent(Event event) {
    if (event instanceof RedrawEvent) {
      mapSwitcherButtons.getChildren().clear();
      drawPath();
    }
  }

  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    handicapMap = new HashMap<>();
    stairsMap = new HashMap<>();

    currentState = AccessabilityState.NONE;

    val clipRect = new Rectangle();
    clipRect.widthProperty().bind(clipper.widthProperty());
    clipRect.heightProperty().bind(clipper.heightProperty());
    clipper.setClip(clipRect);

    JFXDepthManager.setDepth(sideBar, 2);
    nodeMap = database.exportNodes();
    handicapMap = new HashMap<>();
    createHandicap();
    createStairsOnly();
    defaultStart = (Node) nodeMap.values().toArray()[0];
    defaultStop = (Node) nodeMap.values().toArray()[0];
    beginning = defaultStart;
    finish = defaultStop;
    colorPicker.setValue(Color.valueOf("fd8842"));
    nodeMapViewController.setEdgeMovement(true);

    startLocation.setOnNodeSelectedListener(node -> {
      mapSwitcherButtons.getChildren().clear();
      nodeMapViewController.deleteNode(beginning);
      beginning = node;
      nodeMapViewController.draw();
      drawPath();
    });
    stopLocation.setOnNodeSelectedListener(node -> {
      mapSwitcherButtons.getChildren().clear();
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
      if ((!node.getNodeType().equals("STAI") && !node.getNodeType().equals("REST"))
          || (node.getNodeType().equals("REST") && node.getLongName().toLowerCase()
          .contains("handicap"))) {
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

  public void createStairsOnly() {
    //create the map of nodes without edges
    for (Node node : nodeMap.values()) {
      if (!node.getNodeType().equals("ELEV")) {
        val newNode = node.withNeighbors(new LinkedList<>());
        stairsMap.put(newNode.getNodeID(), newNode);
      }
    }
    //populate only edges that still exists
    for (Edge edge : database.exportEdges()) {
      if (stairsMap.containsKey(edge.getStartID()) && stairsMap.containsKey(edge.getStopID())) {
        stairsMap.get(edge.getStartID()).getNeighbors().add(stairsMap.get(edge.getStopID()));
        stairsMap.get(edge.getStopID()).getNeighbors().add(stairsMap.get(edge.getStartID()));
      }
    }
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    mapSwitcherButtons.getChildren().clear();
    nodeMapViewController.clearText();
    nodeMapViewController.deleteNode(beginning);
    nodeMapViewController.deleteNode(finish);
    beginning = defaultStart;
    finish = defaultStop;
    stopLocation.clear();
    startLocation.clear();
    nodeMapViewController.clearText();
    nodeMapViewController.draw();
  }

  private void drawPath() {
    mapSwitcherButtons.getChildren().clear();
    nodeMapViewController.clearText();
    List<Node> nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);
    List<String> floorsCrossed = getPathFloors(nodes);
    for (int i = 0; i < floorsCrossed.size(); i++) {
      val button = new JFXButton();
      button.setButtonType(RAISED);
      button.setText("Step " + (i + 1) + "\n" + floorsCrossed.get(i));
      button.setOnAction(event -> miniMapButtons(event));
      button.setStyle("-fx-background-color: lightgray");
      mapSwitcherButtons.getChildren().add(button);
    }
    nodeMapViewController.clearText();
    nodeMapViewController.addNode(beginning);
    nodeMapViewController.addNode(finish);
    if (beginning.getFloor().equals(nodeMapViewController.getFloor()) && beginning.getBuilding()
        .equals(nodeMapViewController.getBuilding())) {
      nodeMapViewController
          .addText(beginning.getXCoord(), beginning.getYCoord(), beginning.getLongName(),
              "-fx-background-color:lightgray");
    }
    if (finish.getFloor().equals(nodeMapViewController.getFloor()) && finish.getBuilding()
        .equals(nodeMapViewController.getBuilding())) {
      nodeMapViewController.addText(finish.getXCoord(), finish.getYCoord(), finish.getLongName(),
          "-fx-background-color:lightgray");
    }

    for (int i = 0; i < nodes.size() - 1; i++) {
      nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
    }
  }

  @FXML
  private void switchAccessibility(ActionEvent event) {
    resetPath();
    switch (currentState) {
      case NONE:
        if (event.getSource().equals(handicap)) {
          startLocation.setNodes(handicapMap.values());
          stopLocation.setNodes(handicapMap.values());
          handicap.setStyle("-fx-background-color: dodgerblue;");
          currentState = AccessabilityState.HANDICAP;
        } else {
          startLocation.setNodes(stairsMap.values());
          stopLocation.setNodes(stairsMap.values());
          stairsOnly.setStyle("-fx-background-color: seagreen;");
          currentState = AccessabilityState.STAIR;
        }
        break;
      case HANDICAP:
        if (event.getSource().equals(handicap)) {
          startLocation.setNodes(nodeMap.values());
          stopLocation.setNodes(nodeMap.values());
          handicap.setStyle("-fx-background-color: lightgray;");
          currentState = AccessabilityState.NONE;
        } else {
          startLocation.setNodes(stairsMap.values());
          stopLocation.setNodes(stairsMap.values());
          stairsOnly.setStyle("-fx-background-color: seagreen;");
          handicap.setStyle("-fx-background-color: lightgray;");
          currentState = AccessabilityState.STAIR;
        }
        break;
      case STAIR:
        if (event.getSource().equals(handicap)) {
          startLocation.setNodes(handicapMap.values());
          stopLocation.setNodes(handicapMap.values());
          handicap.setStyle("-fx-background-color: dodgerblue;");
          stairsOnly.setStyle("-fx-background-color: lightgray;");
          currentState = AccessabilityState.HANDICAP;
        } else {
          startLocation.setNodes(nodeMap.values());
          stopLocation.setNodes(nodeMap.values());
          stairsOnly.setStyle("-fx-background-color: lightgray;");
          currentState = AccessabilityState.NONE;
        }
        break;
      default:
        snackBar.show("Error: unable to set accessibility mode");
    }

  }

  @FXML
  private void generateQR() {
    val steps = generateSteps();
    try {
      int RGB = getIntFromColor(color.getRed(), color.getGreen(), color.getBlue());
      dialog.showFullscreen(new HBox(new ImageView(WebApp.createQRCode(RGB, steps))));
    } catch (Exception e) {
      log.error("Failed to create a QR code from the current path", e);
      snackBar.show("Failed to create a QR code from the current path");
    }
  }

  @FXML
  private void generateTD() {
    val instructions = createInstructionsFromNodes(
        pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish));
    StringBuilder directions = new StringBuilder();
    for (Instruction i : instructions) {
      directions.append(i.getDirections()).append("\n");
    }
    dialog.showFullscreen(new HBox(new Label(directions.toString())));
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
        if (nextNode.getBuilding().toLowerCase().equals("street")) {
          instructions.add(new Instruction("Go to the street", Icon.NAVIGATION));
        } else {
          instructions.add(new Instruction("Go to floor " + nextNode.getFloor()
              + " in " + nextNode.getBuilding(), Icon.NAVIGATION));
        }
      } else if (!currNode.getFloor().equals(nextNode.getFloor())) {
        instructions.add(new Instruction(
            "Head to floor " + nextNode.getFloor(), Icon.NAVIGATION));
      } else {
        final double pi = Math.PI, seventh = 2 * pi / 7;
        final double vertexX = currNode.getXCoord(), vertexY = currNode.getYCoord();
        val prevTheta = Math.atan2(nextNode.getYCoord() - vertexY, nextNode.getXCoord() - vertexX);
        val nextTheta = Math.atan2(prevNode.getYCoord() - vertexY, prevNode.getXCoord() - vertexX);
        val rawTheta = nextTheta - prevTheta;
        val theta = rawTheta < 0 ? rawTheta + 2 * pi : rawTheta;
        for (double counter = seventh; true; counter += seventh) {
          if (theta < counter) {
            instructions.add(segmentToInstruction((int) Math.round(counter / seventh) - 1,
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

  public void setBGColor() {
    color = colorPicker.getValue();
    nodeMapViewController.setBackgroundColor(String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255)));
  }

  public int getIntFromColor(double r, double g, double b) {
    int R = (int) Math.round(255 * r);
    int G = (int) Math.round(255 * g);
    int B = (int) Math.round(255 * b);

    R = (R << 16) & 0x00FF0000;
    G = (G << 8) & 0x0000FF00;
    B = B & 0x000000FF;

    return 0xFF000000 | R | G | B;
  }

  @FXML
  public void reversePath() {
    val change = beginning;
    beginning = finish;
    finish = change;
    nodeMapViewController.draw();
    if (beginning != null && finish != null) {
      startLocation.setTextWithoutPopup(beginning.getLongName());
      stopLocation.setTextWithoutPopup(finish.getLongName());
    }
    drawPath();
  }

  private List<String> getPathFloors(List<Node> path) {
    List<String> floors = new LinkedList<>();
    String currentFloor = path.get(0).getFloor();
    String currentBuilding = path.get(0).getBuilding();
    floors.add(currentBuilding + ", Floor " + currentFloor);
    for (Node n : path) {
      if (!n.getFloor().equals(currentFloor) || !n.getBuilding().equals(currentBuilding)) {
        currentFloor = n.getFloor();
        currentBuilding = n.getBuilding();
        floors.add(currentBuilding + ", Floor " + currentFloor);
      }
    }
    return floors;
  }

  private void miniMapButtons(ActionEvent event) {
    String fullName = ((JFXButton) event.getSource()).getText();
    fullName = fullName.split("\n")[1];
    String building = fullName.split(", Floor ")[0];
    String floor = fullName.split(", Floor ")[1];
    dispatch(new FloorSwitchEvent(floor, building));
  }

  /**
   * gets the closest node of provided type to beginning
   *
   * @param type                the nodeType desired
   * @param sameFloorOnly       true if the node should be on the same floor as beginning
   * @param shortNameSupplement extra string to check if the short name contains, otherwise ""
   * @return closest node of matching type
   */
  private Node getNearest(String type, boolean sameFloorOnly, String shortNameSupplement) {
    val nodes = new LinkedList<Node>();
    val beenTo = new LinkedList<Node>();
    if (beginning != null) {
      nodes.add(beginning);
      beenTo.add(beginning);

      while (!nodes.isEmpty()) {
        val current = nodes.poll();
        if (current.getNodeType().equals(type) &&
            current.getBuilding().equals(beginning.getBuilding()) &&
            (!sameFloorOnly || current.getFloor().equals(beginning.getFloor())) &&
            (shortNameSupplement.equals("") || current.getShortName()
                .contains(shortNameSupplement))) {
          return current;
        }
        for (val n : current.getNeighbors()) {
          if (!beenTo.contains(n)) {
            nodes.add(n);
            beenTo.add(n);
          }
        }
      }
    }
    return null;
  }

  @FXML
  private void setNearestBathroom() {
    if (!findPathToNearest("REST", true, "")) {
      snackBar.show("Unable to set nearest bathroom");
    }
  }

  @FXML
  private void setNearestExit() {
    if (!findPathToNearest("EXIT", false, "")) {
      snackBar.show("Unable to set nearest exit");
    }
  }

  @FXML
  private void setNearestFood() {
    if (!findPathToNearest("RETL", false, "Food")) {
      snackBar.show("Unable to set nearest food provider");
    }
  }

  /**
   * draws a path to the nearest node matching the parameters
   *
   * @param type                the type of node
   * @param sameFloorOnly       if the node must be on the same floor as beginning
   * @param shortNameSupplement additional string to check for in node shortName
   * @return true if the path is drawn, false otherwise
   */
  private boolean findPathToNearest(String type, boolean sameFloorOnly,
      String shortNameSupplement) {
    nodeMapViewController.deleteNode(finish);
    val nearestNode = getNearest(type, sameFloorOnly, shortNameSupplement);
    if (nearestNode == null) {
      return false;
    }
    finish = nearestNode;
    stopLocation.setTextWithoutPopup(finish.getLongName());
    nodeMapViewController.draw();
    drawPath();
    return true;
  }
}
