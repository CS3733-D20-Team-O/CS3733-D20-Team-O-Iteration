package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
public class NodeMapView extends ViewModelBase {

  private double nodeSize = 5;
  private double edgeSize = 3;
  private final static int maxFloor = 5;
  private final static int minFloor = 1;
  private final static Paint nodeColor = Color.web("#00991f"); // Green
  private final static Paint edgeColor = Color.web("#58A5F0"); // Light blue
  private final static Paint highlightColor = Color.RED; // Red
  private final static double zoomInc = 0.1;
  private final static double dragInc = 2;

  /**
   * The current floor being displayed
   */
  private int floor = minFloor;
  /**
   * The current zoom level
   */
  private double currentZoom = 1.0;
  /**
   * Gets called when a node is left-clicked
   */
  @Setter
  private Consumer<Node> onNodeLeftTapListener;
  /**
   * Gets called when a node is right-clicked
   */
  @Setter
  private Consumer<Node> onNodeRightTapListener;
  /**
   * Gets called when a node is left-click dragged
   */
  @Setter
  private BiConsumer<Node, MouseEvent> onNodeLeftDragListener;
  /**
   * Gets called when a node is right-clicked dragged
   */
  @Setter
  private BiConsumer<Node, MouseEvent> onNodeRightDragListener;
  /**
   * Gets called when a node has been released from a left-click drag
   */
  @Setter
  private BiConsumer<Node, MouseEvent> onNodeLeftDragReleaseListener;
  /**
   * Gets called when a node has been released from a right-click drag
   */
  @Setter
  private BiConsumer<Node, MouseEvent> onNodeRightDragReleaseListener;
  /**
   * Gets called when a edge is left-clicked
   */
  @Setter
  private BiConsumer<Node, Node> onEdgeLeftTapListener;
  /**
   * Gets called when a right-click on the map occurs but it is not on a node
   */
  @Setter
  private BiConsumer<Integer, Integer> onMissRightTapListener;
  /**
   * The node map to use to fuel the display todo make an arraylist todo using a map of integers to
   * something else isn't really the appropriate data structure
   */
  private final Map<Integer, Map<String, Node>> nodeMap = new HashMap<>();

  @FXML
  private ImageView backgroundImage;
  @FXML
  private StackPane floorPane;
  @FXML
  private StackPane nodeGroup, edgeGroup;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    //nodeGroup = new StackPane();
    //edgeGroup = new StackPane();

    backgroundImage.fitWidthProperty().addListener((o, old, newWidth) -> {
      nodeGroup.setPrefWidth(newWidth.doubleValue());
      edgeGroup.setPrefWidth(newWidth.doubleValue());
    });

    backgroundImage.fitHeightProperty().addListener((o, old, newHeight) -> {
      nodeGroup.setPrefHeight(newHeight.doubleValue());
      edgeGroup.setPrefHeight(newHeight.doubleValue());
    });

    backgroundImage.setFitWidth(990.4);
    backgroundImage.setFitHeight(594.4);

    //floorPane.getChildren().addAll(edgeGroup, nodeGroup);

    // Display the current floor (and consequently call the above listeners)
    setFloor(minFloor);

    // Set up event for a scroll event
    floorPane.setOnScroll(event -> {
      if (event.getDeltaY() > 0) { // Zoom out
        zoom(this.currentZoom - zoomInc);
      } else { // Zoom in
        zoom(this.currentZoom + zoomInc);
      }
    });

    // Set up event for when the the background is clicked
    backgroundImage.setOnMouseClicked(event -> { // todo check
      if (onMissRightTapListener != null && event.isSecondaryButtonDown()) {
        val imageX = event.getX() / floorPane.getWidth() * backgroundImage.getImage()
            .getWidth(); // todo fix size
        val imageY = event.getY() / floorPane.getHeight() * backgroundImage.getImage().getHeight();
        onMissRightTapListener.accept((int) imageX, (int) imageY);
      }
    });

    // Set up event for when the background is dragged
    backgroundImage.setOnMouseDragged(event -> {
      if (event.isPrimaryButtonDown()) {
        // todo fix
        floorPane.setTranslateX(floorPane.getTranslateX() - event.getX());
        floorPane.setTranslateY(floorPane.getTranslateY() - event.getY());
      }
    });
  }

  /**
   * Sets a new node map, deletes the previous nodes and draws the new nodes
   *
   * @param nodeMap the new node map to fuel this view
   */
  public void setNodeMap(Map<String, Node> nodeMap) {
    this.nodeMap.clear(); // Clear the current node map and the current nodeGroup and edgeGroup

    nodeMap.keySet().forEach((id) -> placeFloorNode(id, nodeMap.get(id)));
    draw();
  }

  /**
   * Adds a node (and adds it to the current map if on the correct floor)
   *
   * @param node a node
   */
  public void addNode(Node node) {
    placeFloorNode(node.getNodeID(), node);
    if (node.getFloor() == this.floor) {
      drawNode(node);
    }
  }

  /**
   * Deletes a node from the map
   *
   * @param node a node
   */
  public void deleteNode(Node node) {
    if (this.nodeMap.containsKey(node.getFloor())) {
      nodeMap.get(node.getFloor()).remove(node.getNodeID());

      if (node.getFloor() == this.floor) {
        val target = findNode(node);
        if (target
            != null) { // This is essentially making sure if the node just disappeared for no good reason (should we keep this?)
          nodeGroup.getChildren().remove(target);
        }
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
   * Clears the map of nodes/edges and draws all the nodes on a certain floor
   */
  public void draw() {
    // Clear the items displayed
    nodeGroup.getChildren().clear();
    edgeGroup.getChildren().clear();

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
    val x = node.getXCoord() / backgroundImage.getImage().getWidth() * floorPane
        .getWidth(); // todo fix size
    val y = node.getYCoord() / backgroundImage.getImage().getHeight() * floorPane.getHeight();
    val drawnNode = new NodeCircle(node, x, y, nodeSize, nodeColor);
    //System.out.println("Node: [" + node.getNodeID() + "] At X: [" + x + "] At Y: [" + y + "]");
    //System.out.println("Circle properties: At X: [" + drawnNode.getCenterX() + "] At Y: [" + drawnNode.getCenterY() + "]");
    drawnNode.setOnMouseClicked(event -> { // todo Fix click (rightclick is left, left is right)
      if (onNodeLeftTapListener != null && event.isPrimaryButtonDown()) {
        onNodeLeftTapListener.accept(drawnNode.node);
      } else if (onNodeRightTapListener != null && event.isSecondaryButtonDown()) {
        onNodeRightTapListener.accept(drawnNode.node);
      }
    });
    drawnNode.setOnMouseDragged(event -> {
      if (onNodeLeftDragListener != null && event.isPrimaryButtonDown()) {
        onNodeLeftDragListener.accept(drawnNode.node, event);
      } else if (onNodeRightDragListener != null) {
        onNodeRightDragListener.accept(drawnNode.node, event);
      }
    });
    drawnNode.setOnMouseDragExited(event -> {
      if (onNodeLeftDragReleaseListener != null && event.isPrimaryButtonDown()) {
        onNodeLeftDragReleaseListener.accept(drawnNode.node, event);
      } else if (onNodeRightDragReleaseListener != null) {
        onNodeRightDragReleaseListener.accept(drawnNode.node, event);
      }
    });
    nodeGroup.getChildren().add(drawnNode);
  }

  /**
   * Draws an edge (represented as a NodeLine) between the two specified nodes
   *
   * @param n1 the first node
   * @param n2 the second node
   */
  public void drawEdge(Node n1, Node n2) {
    // Only draw this edge if both nodes are on this floor
    if (n1.getFloor() == getFloor() && n2.getFloor() == getFloor()) {
      val x1 = n1.getXCoord() / backgroundImage.getImage().getWidth() * floorPane
          .getWidth(); // todo fix size
      val y1 = n1.getYCoord() / backgroundImage.getImage().getHeight() * floorPane.getHeight();
      val x2 = n2.getXCoord() / backgroundImage.getImage().getWidth() * floorPane.getWidth();
      val y2 = n2.getYCoord() / backgroundImage.getImage().getHeight() * floorPane.getHeight();
      val drawnEdge = new NodeLine(n1, n2, x1, y1, x2, y2);
      drawnEdge.setOnMouseClicked(event -> {
        if (event.isPrimaryButtonDown()) {
          onEdgeLeftTapListener.accept(drawnEdge.node1, drawnEdge.node2);
        }
      });
      drawnEdge.setStrokeWidth(edgeSize);
      drawnEdge.setStroke(edgeColor);
      edgeGroup.getChildren().add(drawnEdge);
    }
  }

  /**
   * Draws an edge (represented as a NodeLine) between the origin and the given nodes
   *
   * @param origin  the origin
   * @param xTarget the given xTarget
   * @param yTarget the given yTarget
   */
  public void drawEdge(Node origin, int xTarget, int yTarget) {
    // todo check the logic with this
    // Only draw this edge if the node is on this floor
    if (origin.getFloor() == getFloor()) {
      val x = origin.getXCoord() / backgroundImage.getImage().getWidth() * floorPane
          .getWidth(); // todo fix size
      val y = origin.getYCoord() / backgroundImage.getImage().getHeight() * floorPane.getHeight();
      val drawnEdge = new NodeLine(origin, null, x, y, xTarget, yTarget);
      drawnEdge.setOnMouseClicked(event -> {
        if (event.isPrimaryButtonDown()) {
          onEdgeLeftTapListener.accept(drawnEdge.node1, drawnEdge.node2);
        }
      });
      drawnEdge.setStrokeWidth(edgeSize);
      drawnEdge.setStroke(edgeColor);
      edgeGroup.getChildren().add(drawnEdge);
    }
  }

  /*
  /**
   * Used to calculate if a click was next to a node or not
   *
   * @param event the MouseEvent
   *
  private void checkClick(MouseEvent event) {
    if (event.isPrimaryButtonDown()) { // Left-click
    } else if (event.isSecondaryButtonDown()) { // Right-click

    }
    val imageX = event.getX() / nodeCanvas.getWidth() * backgroundImage.getImage().getWidth();
    val imageY = event.getY() / nodeCanvas.getHeight() * backgroundImage.getImage().getHeight();

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
    if (closestDistance >= nodeSize) {
      if (onMissTapListener != null) {
        onMissTapListener.accept((int) imageX, (int) imageY);
      }
    } else {
      if (onNodeTappedListener != null) {
        onNodeTappedListener.accept(closest);
      }
    }
  }*/

  /**
   * Zooms the canvas in using this multiplier
   *
   * @param zoom the amount of zoom (1.0 is default)
   */
  public void zoom(double zoom) {
    floorPane.setScaleX(zoom);
    floorPane.setScaleY(zoom);
    this.currentZoom = zoom;
  }

  /**
   * Sets the currently drawn floor to the given floor and updates the NodeMapView
   *
   * @param floor the floor to swap to
   */
  public void setFloor(int floor) {
    if (floor >= minFloor && floor <= maxFloor) {
      this.floor = floor;
      backgroundImage.setImage(new Image("floors/" + floor + ".png"));
    }
    draw();
  }

  /**
   * Increments the current floor to the floor above
   */
  public void incrementFloor() {
    setFloor(this.floor + 1);
  }

  /**
   * Increments the current floor to the floor below
   */
  public void decrementFloor() {
    setFloor(this.floor - 1);
  }

  /**
   * Highlights the current node on the NodeMap
   *
   * @param node the node to highlight
   */
  public void highlightNode(Node node) {
    val nodeCircle = findNode(node);
    if (nodeCircle != null) {
      nodeCircle.setFill(highlightColor);
    }
  }

  /**
   * Un-highlights the current node on the NodeMap
   *
   * @param node the node to stop highlighting
   */
  public void unhighlightNode(Node node) {
    val nodeCircle = findNode(node);
    if (nodeCircle != null) {
      nodeCircle.setFill(nodeColor);
    }
  }

  /**
   * Highlights the current edge on the NodeMap
   *
   * @param edge the edge to highlight
   */
  public void highlightEdge(Edge edge) {
    val nodeLine = findLine(edge);
    if (nodeLine != null) {
      nodeLine.setStroke(highlightColor);
    }
  }

  /**
   * Un-highlights the current edge on the NodeMap
   *
   * @param edge the edge to stop highlighting
   */
  public void unhighlightEdge(Edge edge) {
    val nodeLine = findLine(edge);
    if (nodeLine != null) {
      nodeLine.setStroke(edgeColor);
    }
  }

  /**
   * Allows to set the size of drawn nodes (note: cannot resize edges once they are placed on the
   * map)
   *
   * @param size the multiplier for the size
   */
  public void setNodeSize(double size) {
    this.nodeSize = size;
  }

  /**
   * Allows to set the size of drawn edges (note: cannot resize nodes once they are placed on the
   * map)
   *
   * @param size the multiplier for the size
   */
  public void setEdgeSize(double size) {
    this.edgeSize = size;
  }

  /**
   * Allows for the given node to be relocated
   *
   * @param node the node to be relocated
   * @param x    the new x location
   * @param y    the new y location
   */
  public void relocateNode(Node node, double x, double y) {
    val nodeCircle = findNode(node);
    nodeCircle.setCenterX(x);
    nodeCircle.setCenterY(y);
  }

  /**
   * Make all nodes on the floor visible
   */
  public void makeNodeVisible() {
    nodeGroup.getChildren().forEach(nodeCircle -> {
      nodeCircle.setVisible(true);
    });
  }

  /**
   * Make all nodes on the floor invisible
   */
  public void makeNodeInvisible() {
    nodeGroup.getChildren().forEach(nodeCircle -> {
      nodeCircle.setVisible(false);
    });
  }

  /**
   * Finds a NodeCircle that a given node is represented by
   *
   * @param node the given node
   * @return the nodeCircle that node refers to (or null if no nodeCircle is found)
   */
  private NodeCircle findNode(Node node) {
    for (val child : nodeGroup.getChildren()) {
      val nodeCircle = (NodeCircle) child;
      if (nodeCircle.node.equals(node)) {
        return nodeCircle;
      }
    }
    return null;
  }

  /**
   * Find a NodeLine that an edge is represented by
   *
   * @param edge the given edge
   * @return the NodeLine that node refers to (or null if no NodeLine is found)
   */
  private NodeLine findLine(Edge edge) {
    for (val child : edgeGroup.getChildren()) {
      val nodeLine = (NodeLine) child;
      if (nodeLine.node1.getNodeID().equals(edge.getStartID()) && nodeLine.node2.getNodeID()
          .equals(edge.getStopID())) {
        return nodeLine;
      }
    }
    return null;
  }

  /**
   * Used for referring an object back to what Node it refers to
   */
  private class NodeCircle extends Circle {

    public Node node;

    public NodeCircle(Node node, double x, double y, double nodeSize, Paint nodeColor) {
      super(0, 0, nodeSize, nodeColor);
      setTranslateX(x);
      setTranslateY(y);
      this.node = node;
    }
  }

  /**
   * Used for referring an object back to what Edge it refers to
   */
  private class NodeLine extends Line {

    public Node node1;
    public Node node2;

    public NodeLine(Node node1, Node node2, double x1, double y1, double x2, double y2) {
      super(x1, y1, x2, y2);
      setTranslateX((x1 + x2) / 2);
      setTranslateX((y1 + y2) / 2);
      this.node1 = node1;
      this.node2 = node2;
    }
  }

  // This is really sketchy for testing if you ask me.
  // I think what this does is give a copy of the list, so the list isn't directly interacted with

  /**
   * Used for testing
   *
   * @return a list
   */
  public List<javafx.scene.Node> getNodeGroupList() {
    return new ArrayList<>(nodeGroup.getChildren());
  }

  /**
   * Used for testing
   *
   * @return a list
   */
  public List<javafx.scene.Node> getEdgeGroupList() {
    return new ArrayList<>(edgeGroup.getChildren());
  }
}