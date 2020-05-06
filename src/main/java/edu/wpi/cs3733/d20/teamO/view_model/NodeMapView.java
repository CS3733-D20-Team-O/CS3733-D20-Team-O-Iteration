package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Getter
public class NodeMapView extends ViewModelBase {

  private double nodeSize = 5;
  private double edgeSize = 3;
  private final static Paint nodeColor = Color.web("#00991f"); // Green
  private final static Paint edgeColor = Color.web("#58A5F0"); // Light blue
  private final static Paint highlightColor = Color.RED; // Red
  private final static double zoomInc = 0.1;
  private final static double dragInc = 2;

  private double dragX = 0;
  private double dragY = 0;

  /**
   * The current floor being displayed
   */
  private String floor = "1";
  /**
   * The current building being displayed
   */
  private String building = "Faulkner";
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
  private final Map<String, Map<String, Node>> nodeMap = new HashMap<>();
  // Note: The stored string is a combined string, consisting of the <building name>_<floor name>

  @FXML
  private ImageView backgroundImage;
  @FXML
  private StackPane floorPane;
  @FXML
  private AnchorPane nodeGroup, edgeGroup, dummyLayer;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    //backgroundImage.setFitWidth(990.4);
    //backgroundImage.setFitHeight(594.4);
    backgroundImage.setImage(new Image("floors/Faulkner_1.png"));

    // Set up event for a scroll event
    floorPane.setOnScroll(event -> {
      if (event.getDeltaY() < 0) { // Zoom out
        zoom(this.currentZoom - zoomInc);
      } else { // Zoom in
        zoom(this.currentZoom + zoomInc);
      }
    });

    // Set up event for when the the background is clicked
    nodeGroup.setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.SECONDARY) {
        System.out.println("NodeGroup Right Click");
        val imageX = translateToImageX((int) event.getX());
        val imageY = translateToImageY((int) event.getY());
        onMissRightTapListener.accept(imageX, imageY);
      }
    });

    dummyLayer.setOnMousePressed(event -> {
      if (event.getButton() == MouseButton.PRIMARY) {
        System.out.println("NodeGroup Left Press");
        dragX = floorPane.getLayoutX() - event.getSceneX();
        dragY = floorPane.getLayoutY() - event.getSceneY();
      }
    });

    // Set up event for when the background is dragged
    dummyLayer.setOnMouseDragged(event -> {
      if (event.getButton() == MouseButton.PRIMARY) {
        System.out.println("NodeGroup Left Drag");
        floorPane.setLayoutX(event.getSceneX() + dragX);
        floorPane.setLayoutY(event.getSceneY() + dragY);
      }
    });

    // Display the current floor (and consequently call the above listeners)
    Platform.runLater(() -> setFloor(this.building, this.floor));
  }

  /**
   * Sets a new node map, deletes the previous nodes and draws the new nodes
   *
   * @param nodeMap the new node map to fuel this view
   */
  public void setNodeMap(Map<String, Node> nodeMap) {
    this.nodeMap.clear(); // Clear the current node map and the current nodeGroup and edgeGroup

    nodeMap.keySet().forEach((id) -> System.out.println(
        "[" + id + ", " + nodeMap.get(id).getFloor() + "] (" + nodeMap.get(id).getXCoord() + ", "
            + nodeMap.get(id).getYCoord() + ")"));

    nodeMap.keySet().forEach((id) -> placeFloorNode(id, nodeMap.get(id)));
    System.out.println("============");
    this.nodeMap.get(1).forEach((id, node) -> System.out.println(
        "[" + id + ", " + node.getFloor() + "] (" + node.getXCoord() + ", " + node.getYCoord()
            + ")"));
    draw();
  }

  /**
   * Adds a node (and adds it to the current map if on the correct floor)
   *
   * @param node a node
   */
  public void addNode(Node node) {
    placeFloorNode(node.getNodeID(), node);
    if (node.getFloor().equals(this.floor)) {
      drawNode(node);
    }
  }

  /**
   * Deletes a node from the map
   *
   * @param node a node
   */
  public void deleteNode(Node node) {
    if (this.nodeMap.containsKey(buildingFloorID(node))) {
      nodeMap.get(buildingFloorID(node)).remove(node.getNodeID());

      if (node.getFloor().equals(this.floor)) {
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
    if (!this.nodeMap.containsKey(buildingFloorID(node))) {
      nodeMap.put(buildingFloorID(node), new HashMap<>());
    }
    // Place the node in the correct floor
    nodeMap.get(buildingFloorID(node)).put(string, node);
  }

  /**
   * Clears the map of nodes/edges and draws all the nodes on a certain floor
   */
  public void draw() {
    // Clear the items displayed
    //floorPane.getChildren().clear();
    nodeGroup.getChildren().clear();
    edgeGroup.getChildren().clear();

    //floorPane.getChildren().add(backgroundImage);

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
    val x = translateToPaneX(node.getXCoord());
    val y = translateToPaneY(node.getYCoord());
    val drawnNode = new NodeCircle(node, x, y, nodeSize, nodeColor);
    System.out.println(
        "Node: [" + node.getNodeID() + "] At X: [" + node.getXCoord() + "] At Y: [" + node
            .getYCoord() + "]");
    System.out.println(
        "Circle properties: At X: [" + drawnNode.getCenterX() + "] At Y: [" + drawnNode.getCenterY()
            + "]");
    drawnNode.setOnMouseClicked(event -> {
      if (onNodeLeftTapListener != null && event.getButton() == MouseButton.PRIMARY) {
        System.out.println("Node Left Clicked");
        onNodeLeftTapListener.accept(drawnNode.node);
      } else if (onNodeRightTapListener != null && event.getButton() == MouseButton.SECONDARY) {
        System.out.println("Node Right Clicked");
        onNodeRightTapListener.accept(drawnNode.node);
      }
    });
    drawnNode.setOnMouseDragged(event -> {
      if (onNodeLeftDragListener != null && event.getButton() == MouseButton.PRIMARY) {
        System.out.println("Node Left Dragged");
        onNodeLeftDragListener.accept(drawnNode.node, event);
      } else if (onNodeRightDragListener != null && event.getButton() == MouseButton.SECONDARY) {
        System.out.println("Node Right Dragged");
        onNodeRightDragListener.accept(drawnNode.node, event);
      }
    });
    drawnNode.setOnMouseDragExited(event -> {
      if (onNodeLeftDragReleaseListener != null && event.getButton() == MouseButton.PRIMARY) {
        System.out.println("Node Left Dragged End");
        onNodeLeftDragReleaseListener.accept(drawnNode.node, event);
      } else if (onNodeRightDragReleaseListener != null
          && event.getButton() == MouseButton.SECONDARY) {
        System.out.println("Node Right Dragged End");
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
    if (n1.getFloor().equals(getFloor()) && n2.getFloor().equals(getFloor())) {
      val x1 = translateToPaneX(n1.getXCoord());
      val y1 = translateToPaneY(n1.getYCoord());
      val x2 = translateToPaneX(n2.getXCoord());
      val y2 = translateToPaneY(n2.getYCoord());
      //System.out.println("Drawing an edge (node reference) from (" + translateToPaneX(n1.getXCoord()) + ", " + translateToPaneY(n1.getYCoord()) + ") to (" + translateToPaneX(n2.getYCoord()) + ", " + translateToPaneY(n2.getYCoord()) + ")");
      //System.out.println("Drawing an edge from (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ")");
      val drawnEdge = new NodeLine(n1, n2, x1, y1, x2, y2);
      drawnEdge.setOnMouseClicked(event -> {
        if (onEdgeLeftTapListener != null && event.getButton() == MouseButton.PRIMARY) {
          System.out.println("Edge Left Click");
          onEdgeLeftTapListener.accept(drawnEdge.node1, drawnEdge.node2);
        }
      });

      drawnEdge.setStrokeWidth(edgeSize);
      drawnEdge.setStroke(edgeColor);
      Platform.runLater(() -> edgeGroup.getChildren().add(drawnEdge));
      //edgeGroup.getChildren().add(1, drawnEdge);
    }
  }

  /*
  /**
   * Draws an edge (represented as a NodeLine) between the origin and the given nodes
   *
   * @param origin  the origin
   * @param xTarget the given xTarget
   * @param yTarget the given yTarget

  public void drawEdge(Node origin, int xTarget, int yTarget) {
    // todo check the logic with this
    // Only draw this edge if the node is on this floor
    if (origin.getFloor() == getFloor()) {
      val x = translateToPaneX(origin.getXCoord());
      val y = translateToPaneY(origin.getYCoord());
      val drawnEdge = new NodeLine(origin, null, x, y, xTarget, yTarget);
      drawnEdge.setOnMouseClicked(event -> {
        if (onEdgeLeftTapListener != null && event.getButton() == MouseButton.PRIMARY) {
          onEdgeLeftTapListener.accept(drawnEdge.node1, drawnEdge.node2);
        }
      });
      drawnEdge.setStrokeWidth(edgeSize);
      drawnEdge.setStroke(edgeColor);
      edgeGroup.getChildren().add(drawnEdge);
      //edgeGroup.getChildren().add(1, drawnEdge);
    }
  }/*

  /**
   * Zooms the canvas in using this multiplier
   *
   * @param zoom the amount of zoom (1.0 is default)
   */
  public void zoom(double zoom) {
    if (zoom > 0.5 && zoom < 1.5) {
      floorPane.setScaleX(zoom);
      floorPane.setScaleY(zoom);
      this.currentZoom = zoom;
    }
  }

  /**
   * Sets the currently drawn floor to the given floor and updates the NodeMapView
   *
   * @param building the building the floor is on
   * @param floor    the floor to swap to
   */
  public void setFloor(String building, String floor) {
    try {
      // If the background image doesn't load, don't even bother with the rest.
      backgroundImage.setImage(new Image("floors/" +
          building.replaceAll("\\s+", "") + "_" +
          floor.replaceAll("\\s+", "") + ".png"));
      this.floor = floor;
      draw();
    } catch (Exception e) {
      log.error("The floor map associated with that floor doesn't exist!");
    }
  }

  /**
   * Increments the current floor to the floor above
   */
  // todo remove these two once the editor and pathfinder no longer use it.
  @Deprecated
  public void incrementFloor() {
    //setFloor(this.floor + 1);
  }

  /**
   * Increments the current floor to the floor below
   */
  @Deprecated
  public void decrementFloor() {
    //  setFloor(this.floor - 1);
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
    val floor = nodeMap.get(buildingFloorID(node));
    if (floor != null) {
      floor.remove(node.getNodeID());
      Node newNode = new Node(node.getNodeID(), node.getXCoord(),
          node.getYCoord(), node.getFloor(), node.getBuilding(),
          node.getNodeType(), node.getLongName(), node.getShortName());
      floor.put(node.getNodeID(), newNode);
      System.out.println("Updated node");
    }

    val nodeCircle = findNode(node);
    if (nodeCircle != null) {
      System.out.println("Node: [" + node.getNodeID() + "] At X: [" + x + "] At Y: [" + y + "]");
      System.out.println(
          "Circle properties: At X: [" + x + "] At Y: [" + y
              + "]");
      nodeCircle.setCenterX(translateToPaneX((int) x));
      nodeCircle.setCenterY(translateToPaneY((int) y));
    }
  }

  /**
   * Make all nodes on the floor visible
   */
  public void makeNodeVisible() {
    nodeGroup.getChildren().forEach(nodeCircle -> {
      nodeCircle.setVisible(true);
    });

    /*
    floorPane.getChildren().stream().filter(n -> n instanceof NodeCircle).
        forEach(nodeCircle -> nodeCircle.setVisible(true));*/
  }

  /**
   * Make all nodes on the floor invisible
   */
  public void makeNodeInvisible() {
    nodeGroup.getChildren().forEach(nodeCircle -> {
      nodeCircle.setVisible(false);
    });

    /*
    floorPane.getChildren().stream().filter(n -> n instanceof NodeCircle).
        forEach(nodeCircle -> nodeCircle.setVisible(false));*/
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
      if (nodeCircle.node.getNodeID().equals(node.getNodeID())) {
        return nodeCircle;
      }
    }
    return null;

    /*
    return floorPane.getChildren().stream()
        .filter(n -> n instanceof NodeCircle)
        .map(n -> (NodeCircle) n)
        .reduce(null, (result, current) -> current.node.equals(node) ? current : result);*/
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

    /*return floorPane.getChildren().stream()
        .filter(n -> n instanceof NodeLine)
        .map(n -> (NodeLine) n)
        .reduce(null, (result, current) ->
            current.node1.getNodeID().equals(edge.getStartID()) &&
                current.node2.getNodeID().equals(edge.getStopID()) ? current : result);*/
  }

  public int translateToImageX(int x) {
    System.out.println("FloorPaneWidth: [" + floorPane.getWidth() + "]");
    System.out.println("FloorPaneHeight: [" + floorPane.getHeight() + "]");
    /*System.out.println("BackgroundImageWidth: [" + backgroundImage.getImage().getWidth() + "]");
    System.out.println("BackgroundImageHeight: [" + backgroundImage.getImage().getHeight() + "]");*/

    return (int) (x / getRealWidth() * backgroundImage.getImage().getWidth());
  }

  public int translateToImageY(int y) {
    System.out.println("FloorPaneWidth: [" + floorPane.getWidth() + "]");
    System.out.println("FloorPaneHeight: [" + floorPane.getHeight() + "]");
    /*System.out.println("BackgroundImageWidth: [" + backgroundImage.getImage().getWidth() + "]");
    System.out.println("BackgroundImageHeight: [" + backgroundImage.getImage().getHeight() + "]");*/
    return (int) (y / getRealHeight() * backgroundImage.getImage().getHeight());
  }

  public int translateToPaneX(int x) {
    System.out.println("FloorPaneWidth: [" + floorPane.getWidth() + "]");
    System.out.println("FloorPaneHeight: [" + floorPane.getHeight() + "]");
    /*System.out.println("BackgroundImageWidth: [" + backgroundImage.getImage().getWidth() + "]");
    System.out.println("BackgroundImageHeight: [" + backgroundImage.getImage().getHeight() + "]");*/
    return (int) (x / backgroundImage.getImage().getWidth() * getRealWidth());
  }

  public int translateToPaneY(int y) {
    System.out.println("FloorPaneWidth: [" + floorPane.getWidth() + "]");
    System.out.println("FloorPaneHeight: [" + floorPane.getHeight() + "]");
    /*System.out.println("BackgroundImageWidth: [" + backgroundImage.getImage().getWidth() + "]");
    System.out.println("BackgroundImageHeight: [" + backgroundImage.getImage().getHeight() + "]");*/
    return (int) (y / backgroundImage.getImage().getHeight() * getRealHeight());
  }

  private double getRealWidth() {
    double aspectRatio =
        backgroundImage.getImage().getWidth() / backgroundImage.getImage().getHeight();
    return Math.min(backgroundImage.getFitWidth(), backgroundImage.getFitHeight() * aspectRatio);
  }

  private double getRealHeight() {
    double aspectRatio =
        backgroundImage.getImage().getWidth() / backgroundImage.getImage().getHeight();
    return Math.min(backgroundImage.getFitHeight(), backgroundImage.getFitWidth() / aspectRatio);
  }

  /**
   * Creates a string from the building and floor of a node (used for consistency, I don't trust
   * myself, lmao)
   *
   * @param node The given node
   * @return a string
   */
  private String buildingFloorID(Node node) {
    return node.getBuilding() + "_" + node.getFloor();
  }

  /**
   * Used for referring an object back to what Node it refers to
   */
  private class NodeCircle extends Circle {

    public Node node;

    public NodeCircle(Node node, double x, double y, double nodeSize, Paint nodeColor) {
      super(x, y, nodeSize, nodeColor);
      //setTranslateX(x);
      //setTranslateY(y);
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
      //setTranslateX((x1 + x2) / 2);
      //setTranslateY((y1 + y2) / 2);
      this.node1 = node1;
      this.node2 = node2;
    }
  }
}