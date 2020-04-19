package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
public class NodeMapView extends ViewModelBase {

  /**
   * The current floor being displayed
   */
  private SimpleIntegerProperty floor = new SimpleIntegerProperty(1);
  /**
   * The set maximum floor
   */
  private int maxFloor = 1;
  /**
   * The node map to use to fuel the display
   */
  private Map<String, Node> nodeMap = new HashMap<>();
  /**
   * Gets called when a node is clicked
   */
  @Setter
  private Consumer<Node> onNodeTappedListener;
  /**
   * Gets called when a click on the map occurs but it is not on a node
   */
  @Setter
  private BiConsumer<Integer, Integer> onMissTapListener;

  @FXML
  StackPane mapView;
  @FXML
  ImageView backgroundImage;
  @FXML
  Canvas nodeCanvas;
  @FXML
  Button btnIncrementFloor;
  @FXML
  Button btnDecrementFloor;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // todo import floor into ImageView and set up other basic stuff
    nodeCanvas = new Canvas();
    backgroundImage = new ImageView();
    mapView = new StackPane();
    mapView.getChildren().addAll(backgroundImage, nodeCanvas);

    draw();

    // Set up event for when a node is selected
    nodeCanvas.setOnMouseClicked(event -> {

    });
  }

  /*
  todo
  image view for floor
  canvas for nodes and edges
  use a StackPane to put canvas on top of ImageView
  have a simple floor switcher like in the mockup. Your choice at either top or bottom of the view
  listen for an on tap in the canvas
    - fire either onNodeTappedListener if a node was tapped or the other one
    - calculate distance from finger to node via Math.hypot
    - go through the nodeMap and find the closest node (if map isnt empty)
    - and if the closest node is within the radius + ~10 of each drawn node
    - call on node tapped, otherwise call the other listener with the coordinates
    - to call a listener, call its .accept() method
   */

  /**
   * Sets a new node map and draws the new nodes
   *
   * @param nodeMap the new node map to fuel this view
   */
  public void setNodeMap(Map<String, Node> nodeMap) {
    this.nodeMap = nodeMap;
    draw();
  }

  /**
   * Clears the canvas and draws all the nodes
   */
  private void draw() {
    // Clear the canvas so we can draw fresh
    // todo canvas.clear() or something like that I am guessing. Ask Collin -- he knows
    GraphicsContext nodeGC = nodeCanvas.getGraphicsContext2D();
    nodeGC.clearRect(0, 0, nodeCanvas.getWidth(), nodeCanvas.getHeight());
    // Draw all the nodes
    nodeMap.keySet().forEach((id) -> drawNode(nodeMap.get(id)));
  }

  /**
   * @param node the node to draw to the canvas
   */
  private void drawNode(Node node) {
    // Only paint this node if it is on this floor (otherwise we will have other floors' nodes)
    if (node.getFloor() == getFloor()) {
      GraphicsContext nodeGC = nodeCanvas.getGraphicsContext2D();
      nodeGC.setFill(Color.RED);
      nodeGC.fillOval(node.getXCoord(), node.getYCoord(), 10, 10);
    }
  }

  /**
   * Draws an edge (represented as a line) between the two specified nodes
   *
   * @param n1 the first node
   * @param n2 the second node
   */
  public void drawEdge(Node n1, Node n2) {
    // Only draw this edge if both nodes are on this floor
    if (n1.getFloor() == getFloor() && n2.getFloor() == getFloor()) {
      GraphicsContext nodeGC = nodeCanvas.getGraphicsContext2D();
      nodeGC.setStroke(Color.BLUE);
      nodeGC.setLineWidth(3.0);
      nodeGC.strokeLine(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());

      // As drawing a line between the two points will lay on top of the nodes,
      //  redraw the nodes to be on top of the newly drawn line
      drawNode(n1);
      drawNode(n2);
    }
  }

  // todo you can link this up to the next floor button
  @FXML
  private void incrementFloor() {
    if (getFloor() < maxFloor) {
      val floor = getFloor() + 1;
      // todo update ImageView
      draw();
      setFloor(floor);
    }
  }

  // todo you can link this up to the previous floor button
  @FXML
  private void decrementFloor() {
    if (getFloor() > 1) {
      val floor = getFloor() - 1;
      // todo update ImageView
      draw();
      setFloor(floor);
    }
  }

  /**
   * Increments the max floor
   */
  public void incrementMaxFloor() {
    maxFloor++;
  }

  /**
   * Decrements the max floor (note: cannot decrement past one)
   */
  public void decrementMaxFloor() {
    if (maxFloor > 1) {
      maxFloor--;
    }
  }

  public int getFloor() {
    return floor.get();
  }

  public void setFloor(int floor) {
    this.floor.set(floor);
  }

  public SimpleIntegerProperty floorProperty() {
    return floor;
  }
}
