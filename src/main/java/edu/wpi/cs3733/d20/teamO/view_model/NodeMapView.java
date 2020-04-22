package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
public class NodeMapView extends ViewModelBase {

  private final static double nodeSize = 10;
  private final static int maxFloor = 5;
  private final static int minFloor = 1;

  /**
   * The current floor being displayed
   */
  private int floor = minFloor;
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
  /**
   * The node map to use to fuel the display todo make an arraylist todo using a map of integers to
   * something else isn't really the appropriate data structure
   */
  private final Map<Integer, Map<String, Node>> nodeMap = new HashMap<>();

  @FXML
  private ImageView backgroundImage;
  @FXML
  private Canvas nodeCanvas;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Display the current floor (and consequently call the above listeners)
    setFloor(minFloor);

    // Set up event for when a node is selected (or not selected)
    nodeCanvas.setOnMouseClicked(event -> checkClick((int) event.getX(), (int) event.getY()));
  }

  /**
   * Sets a new node map and draws the new nodes
   *
   * @param nodeMap the new node map to fuel this view
   */
  public void setNodeMap(Map<String, Node> nodeMap) {
    this.nodeMap.clear(); // Clear the current node map
    nodeMap.keySet().forEach((id) -> placeFloorNode(id, nodeMap.get(id)));
    draw();
  }

  /**
   * Adds a node (and redraws if on the current floor)
   *
   * @param node a node
   */
  public void addNode(Node node) {
    placeFloorNode(node.getNodeID(), node);
    if (node.getFloor() == this.floor) {
      draw();
    }
  }

  /**
   * Deletes a node (and redraws if on the current floor)
   *
   * @param node a node
   */
  public void deleteNode(Node node) {
    if (this.nodeMap.containsKey(node.getFloor())) {
      nodeMap.get(node.getFloor()).remove(node.getNodeID());
      if (node.getFloor() == this.floor) {
        draw();
      }
    }
  }

  /**
   * Places a node into it's proper floor in the nodeMap
   *
   * @param string a given string for a node
   * @param node   the node to be placed into the nodeMap
   */
  private void placeFloorNode(String string, Node node) {
    // Make the new floor if it doesn't exist
    if (!this.nodeMap.containsKey(node.getFloor())) {
      nodeMap.put(node.getFloor(), new HashMap<>());
    }
    // Place the node in the correct floor
    nodeMap.get(node.getFloor()).put(string, node);
  }

  /**
   * Clears the canvas and draws all the nodes on a certain floor
   */
  private void draw() {
    // Clear the canvas so we can draw fresh
    val nodeGC = nodeCanvas.getGraphicsContext2D();
    nodeGC.clearRect(0, 0, nodeCanvas.getWidth(), nodeCanvas.getHeight());
    // Draw all the nodes on a certain floor
    val floorMap = nodeMap.get(getFloor());
    // Check if nodes for the floor exist
    if (floorMap != null) {
      floorMap.keySet().forEach((id) -> drawNode(floorMap.get(id)));
    }
  }

  /**
   * @param node the node to draw to the canvas
   */
  private void drawNode(Node node) {
    val nodeGC = nodeCanvas.getGraphicsContext2D();
    nodeGC.setFill(Color.RED);
    val x = node.getXCoord() / backgroundImage.getImage().getWidth() * nodeCanvas.getWidth();
    val y = node.getYCoord() / backgroundImage.getImage().getHeight() * nodeCanvas.getHeight();
    nodeGC.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
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
      nodeGC.setStroke(Color.GREEN);
      nodeGC.setLineWidth(3.0);
      val x1 = n1.getXCoord() / backgroundImage.getImage().getWidth() * nodeCanvas.getWidth();
      val y1 = n1.getYCoord() / backgroundImage.getImage().getHeight() * nodeCanvas.getHeight();
      val x2 = n2.getXCoord() / backgroundImage.getImage().getWidth() * nodeCanvas.getWidth();
      val y2 = n2.getYCoord() / backgroundImage.getImage().getHeight() * nodeCanvas.getHeight();
      nodeGC.strokeLine(x1, y1, x2, y2);

      // As drawing a line between the two points will lay on top of the nodes,
      //  redraw the nodes to be on top of the newly drawn line
      drawNode(n1);
      drawNode(n2);
    }
  }

  /**
   * Used to calculate if a click was next to a node or not
   *
   * @param x the x coordinate of the MouseEvent
   * @param y the y coordinate of the MouseEvent
   */
  private void checkClick(int x, int y) {
    // todo offset the x and y
    // todo check drawing nodes at corners (and ensuring they only get a quarter drawn)
    val imageX = x / nodeCanvas.getWidth() * backgroundImage.getImage().getWidth();
    val imageY = y / nodeCanvas.getHeight() * backgroundImage.getImage().getHeight();

    // Setup the closest node algorithm
    Node closest = null;
    double closestDistance = Double.MAX_VALUE;

    // Search for the closest node
    val floorMap = nodeMap.get(getFloor());
    if (floorMap != null) {
      for (val id : floorMap.keySet()) {
        val node = floorMap.get(id);
        val distance = Math.hypot(imageX - node.getXCoord(), imageY - node.getYCoord());
        if (distance < closestDistance) {
          closest = node;
          closestDistance = distance;
        }
      }
    }

    // Call the appropriate listener
    if (closestDistance > nodeSize / 2) {
      if (onMissTapListener != null) {
        onMissTapListener.accept(x, y);
      }
    } else {
      if (onNodeTappedListener != null) {
        onNodeTappedListener.accept(closest);
      }
    }
  }

  public void setFloor(int floor) {
    if (floor >= minFloor && floor <= maxFloor) {
      this.floor = floor;
      backgroundImage.setImage(new Image("floors/" + floor + ".png"));
    }
  }

  public void incrementFloor() {
    setFloor(this.floor + 1);
  }

  public void decrementFloor() {
    setFloor(this.floor - 1);
  }
}
