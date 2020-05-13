package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.RedrawEvent;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Getter
public class NodeMapView extends ViewModelBase {

  // Floor Storage
  @Getter
  private String floor = "1";
  @Getter
  private String building = "Faulkner";

  // Zoom Controls
  private final DoubleProperty currentZoom = new SimpleDoubleProperty(1.0);
  private final static double ZOOM_INC = 0.1;

  // Drag controls
  private double dragX = 0;
  private double dragY = 0;

  // Edge Movement
  @Setter
  private boolean edgeMovement = false;

  // Node and edge visuals
  @Setter
  private double nodeSize = 5, nodeOutlineSize = 2, edgeSize = 3;
  @Setter
  private Paint nodeColor = Color.GREENYELLOW,
      nodeOutlineColor = Color.web("#00991f"),
      edgeColor = Color.web("#58A5F0"),
      highlightColor = Color.RED;

  // Listeners
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
  private NodeCoordConsumer onNodeLeftDragListener;
  /**
   * Gets called when a node is right-clicked dragged
   */
  @Setter
  private NodeCoordConsumer onNodeRightDragListener;
  /**
   * Gets called when a node has been released from a left-click
   */
  @Setter
  private NodeCoordConsumer onNodeLeftTapReleaseListener;
  /**
   * Gets called when a node has been released from a right-click
   */
  @Setter
  private NodeCoordConsumer onNodeRightTapReleaseListener;
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

  // Node storage
  /**
   * The node map to use to fuel the display (each Map of nodes is stored to a floor)
   */
  private final Map<String, Map<String, Node>> nodeMap = new HashMap<>();
  // Note: The stored string is a combined string, consisting of the <building name>_<floor name>

  @FXML
  private ImageView backgroundImage;
  @FXML
  private StackPane floorPane;
  @FXML
  private AnchorPane nodeGroup, edgeGroup, colorLayer, textLayer, dragLayer;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    // So that panes can be clicked through
    nodeGroup.setPickOnBounds(false);
    edgeGroup.setPickOnBounds(false);
    textLayer.setPickOnBounds(false);

    // Set default values
    backgroundImage.setImage(new Image("floors/Faulkner_1.png"));
    setBackgroundColor("#fd8842");

    // Set up event for a scroll event
    floorPane.scaleXProperty().bind(this.currentZoom);
    floorPane.scaleYProperty().bind(this.currentZoom);
    floorPane.setOnScroll(event -> {
      if (event.getDeltaY() < 0 && this.currentZoom.get() > 0.5) { // Zoom out
        this.currentZoom.set(this.currentZoom.get() - ZOOM_INC);
      } else if (this.currentZoom.get() < 4.0) { // Zoom in
        this.currentZoom.set(this.currentZoom.get() + ZOOM_INC);
      }
    });

    // Set up event for when the the background is clicked
    dragLayer.setOnMousePressed(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) { // Used for dragging
        dragX = floorPane.getTranslateX() - event.getSceneX();
        dragY = floorPane.getTranslateY() - event.getSceneY();
      } else if (onMissRightTapListener != null &&
          event.getButton()
              .equals(MouseButton.SECONDARY)) { // Used for right click on the background
        val imageX = translateToImageX((int) event.getX());
        val imageY = translateToImageY((int) event.getY());
        onMissRightTapListener.accept(imageX, imageY);
      }
    });

    // Set up event for when the background is dragged
    dragLayer.setOnMouseDragged(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        if (checkXWithinBounds(event.getSceneX())) {
          floorPane.setTranslateX(event.getSceneX() + dragX);
        }
        if (checkYWithinBounds(event.getSceneY())) {
          floorPane.setTranslateY(event.getSceneY() + dragY);
        }
      }
    });

    // Display the current floor (and consequently call the above listeners)
    Platform.runLater(() -> setFloor(this.building, this.floor));
  }

  /**
   * Used to check for dragging bounds
   *
   * @param x x coordinate
   * @return if within bounds, return true, else return false
   */
  private boolean checkXWithinBounds(double x) {
    // Uses the current location and zoom to check if the area being moved into is within bounds
    double newLocation = x + dragX;
    double zoomMultiplier = (this.currentZoom.get() - 1.0);
    double limit = (backgroundImage.getFitWidth() / 2);

    if (newLocation - (zoomMultiplier * limit) > limit) {
      return false;
    } else {
      return !(newLocation + (zoomMultiplier * limit) < 0 - limit);
    }
  }

  /**
   * Used to check for dragging bounds
   *
   * @param y y coordinate
   * @return if within bounds, return true, else return false
   */
  private boolean checkYWithinBounds(double y) {
    // Uses the current location and zoom to check if the area being moved into is within bounds
    double newLocation = y + dragY;
    double zoomMultiplier = (this.currentZoom.get() - 1.0);
    double limit = (backgroundImage.getFitHeight() / 2);

    if (newLocation - (zoomMultiplier * limit) > limit) {
      return false;
    } else {
      return !(newLocation + (zoomMultiplier * limit) < 0 - limit);
    }
  }

  /**
   * Zooms the canvas in using this multiplier
   *
   * @param zoom the amount of zoom (1.0 is default)
   */
  @Deprecated
  public void zoom(double zoom) {
    if (zoom > 0.5 && zoom < 4.0) {
      this.currentZoom.set(zoom);
    }
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
    nodeGroup.getChildren().clear();
    edgeGroup.getChildren().clear();

    // Draw all the nodes on a certain floor
    val floorMap = nodeMap.get(this.building + "_" + this.floor);
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
    drawnNode.setStroke(nodeOutlineColor);
    drawnNode.setStrokeWidth(nodeOutlineSize);

    drawnNode.setOnMouseReleased(event -> {
      if (onNodeLeftTapListener != null && event.getButton().equals(MouseButton.PRIMARY)) {
        onNodeLeftTapListener.accept(drawnNode.node);
      } else if (onNodeRightTapListener != null && event.getButton()
          .equals(MouseButton.SECONDARY)) {
        onNodeRightTapListener.accept(drawnNode.node);
      }
    });
    drawnNode.setOnMouseDragged(event -> {
      if (onNodeLeftDragListener != null && event.getButton().equals(MouseButton.PRIMARY)) {
        onNodeLeftDragListener.accept(drawnNode.node, translateToImageX((int) event.getX()),
            translateToImageY((int) event.getY()));
      } else if (onNodeRightDragListener != null && event.getButton()
          .equals(MouseButton.SECONDARY)) {
        onNodeRightDragListener.accept(drawnNode.node, translateToImageX((int) event.getX()),
            translateToImageY((int) event.getY()));
      }
    });
    Tooltip.install(
        drawnNode,
        new Tooltip(node.getNodeType() + "//" + node.getLongName())
    );
    nodeGroup.getChildren().add(drawnNode);
  }

  /**
   * Adds a node (and adds it to the current map if on the correct floor)
   *
   * @param node a node
   */
  public void addNode(Node node) {
    placeFloorNode(node.getNodeID(), node);
    if (node.getFloor().equals(this.floor) && node.getBuilding().equals(this.building)) {
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
    }

    val target = findNode(node);
    if (target != null) {
      nodeGroup.getChildren().remove(target);
    }

  }

  /**
   * Updates a oldNode with a newNode
   *
   * @param oldNode the old node
   * @param newNode the new node
   */
  public void updateNode(Node oldNode, Node newNode) {
    val floor = nodeMap.get(buildingFloorID(oldNode));
    if (floor != null && buildingFloorID(oldNode).equals(buildingFloorID(newNode))) {
      floor.remove(oldNode.getNodeID());
      floor.put(newNode.getNodeID(), newNode);

      for (Node nodeItem : newNode.getNeighbors()) {
        relocateEdge(newNode, nodeItem);
      }
    }

    val nodeCircle = findNode(oldNode);
    if (nodeCircle != null) {
      nodeCircle.node = newNode;
      nodeCircle.setCenterX(translateToPaneX(newNode.getXCoord()));
      nodeCircle.setCenterY(translateToPaneY(newNode.getYCoord()));
    }
  }

  /**
   * Highlights the current node on the NodeMap
   *
   * @param node the node to highlight
   */
  public void highlightNode(Node node) {
    colorNode(node, highlightColor);
  }

  /**
   * Un-highlights the current node on the NodeMap
   *
   * @param node the node to stop highlighting
   */
  public void unhighlightNode(Node node) {
    colorNode(node, nodeColor);
  }

  /**
   * Colors the given node with the given color
   *
   * @param node  the node to color
   * @param paint the color
   */
  public void colorNode(Node node, Paint paint) {
    val nodeCircle = findNode(node);
    if (nodeCircle != null) {
      nodeCircle.setFill(paint);
    }
  }

  /**
   * Draws an edge (represented as a NodeLine) between the two specified nodes
   *
   * @param n1 the first node
   * @param n2 the second node
   */
  public void drawEdge(Node n1, Node n2) {
    // Only draw this edge if both nodes are on this floor
    if (n1.getFloor().equals(this.floor) && n1.getBuilding().equals(this.building) &&
        n2.getFloor().equals(this.floor) && n2.getBuilding().equals(this.building)) {
      // Only draw this if the line doesn't exist
      if (findLine(n1.getNodeID(), n2.getNodeID()) == null) {
        val x1 = translateToPaneX(n2.getXCoord());
        val y1 = translateToPaneY(n2.getYCoord());
        val x2 = translateToPaneX(n1.getXCoord());
        val y2 = translateToPaneY(n1.getYCoord());
        val drawnEdge = new NodeLine(n1, n2, x1, y1, x2, y2);
        drawnEdge.setOnMouseClicked(event -> {
          if (onEdgeLeftTapListener != null && event.getButton().equals(MouseButton.PRIMARY)) {
            onEdgeLeftTapListener.accept(drawnEdge.node1, drawnEdge.node2);
          }
        });

        drawnEdge.setStrokeWidth(edgeSize);
        drawnEdge.setStroke(edgeColor);

        // For animation purposes
        if (edgeMovement) {
          drawnEdge.getStrokeDashArray().setAll(10d, 10d);

          final double maxOffset = drawnEdge.getStrokeDashArray().stream()
              .reduce(0d, Double::sum);

          Timeline timeline = new Timeline(
              new KeyFrame(
                  Duration.ZERO,
                  new KeyValue(
                      drawnEdge.strokeDashOffsetProperty(),
                      0,
                      Interpolator.LINEAR)),

              new KeyFrame(
                  Duration.seconds(2),
                  new KeyValue(
                      drawnEdge.strokeDashOffsetProperty(),
                      maxOffset,
                      Interpolator.LINEAR)));

          timeline.setCycleCount(Timeline.INDEFINITE);
          timeline.play();
        }

        Platform.runLater(() -> edgeGroup.getChildren().add(drawnEdge));
      }
    }
  }

  /**
   * Deletes an edge from the map
   *
   * @param n1 the first node in the edge
   * @param n2 the second node in the edge
   */
  public void deleteEdge(Node n1, Node n2) {
    val target = findLine(n1.getNodeID(), n2.getNodeID());
    if (target != null) {
      edgeGroup.getChildren().remove(target);
    }
  }

  /**
   * Given the two nodes, find the edge associated with them and relocate it.
   *
   * @param n1 the first node
   * @param n2 the second node
   */
  public void relocateEdge(Node n1, Node n2) {
    val floor = nodeMap.get(buildingFloorID(n1));
    if (floor != null && n1.getFloor().equals(n2.getFloor()) &&
        n1.getBuilding().equals(n2.getBuilding())) {
      val nodeLine = findLine(n1.getNodeID(), n2.getNodeID());
      if (nodeLine != null) {
        nodeLine.setStartX(translateToPaneX(n1.getXCoord()));
        nodeLine.setStartY(translateToPaneY(n1.getYCoord()));

        nodeLine.setEndX(translateToPaneX(n2.getXCoord()));
        nodeLine.setEndY(translateToPaneY(n2.getYCoord()));
      }
    }
  }

  /**
   * Clears all edges on the screen
   */
  public void clearEdge() {
    edgeGroup.getChildren().clear();
  }

  /**
   * Highlights the current edge on the NodeMap
   *
   * @param edge the edge to highlight
   */
  public void highlightEdge(Edge edge) {
    colorEdge(edge, highlightColor);
  }

  /**
   * Un-highlights the current edge on the NodeMap
   *
   * @param edge the edge to stop highlighting
   */
  public void unhighlightEdge(Edge edge) {
    colorEdge(edge, edgeColor);
  }

  /**
   * Colors the given edge with the given color
   *
   * @param edge  the edge to color
   * @param paint the color
   */
  public void colorEdge(Edge edge, Paint paint) {
    val nodeLine = findLine(edge.getStartID(), edge.getStopID());
    if (nodeLine != null) {
      nodeLine.setStroke(paint);
    }
  }

  /**
   * Sets the background color of the map
   *
   * @param color the color given
   */
  public void setBackgroundColor(String color) {
    colorLayer.setStyle("-fx-background-color: " + color);
  }

  /**
   * Adds the text centered at the given location
   *
   * @param x      the x coordinate (in image coords)
   * @param y      the y coordinate (in image coords)
   * @param string the written text
   * @param style  the text style
   */
  public void addText(int x, int y, String string, String style) {

    val text = new Label(string);
    text.setStyle(style);

    val pane = new StackPane();
    pane.getChildren().addAll(text);

    text.widthProperty().addListener(observable ->
        pane.setLayoutX(translateToPaneX(x) - (text.getWidth() / 2)));

    text.heightProperty().addListener(observable ->
        pane.setLayoutY(translateToPaneY(y) - (text.getHeight() / 2)));

    // Even if the text is dragged on, it should still be able to move the map
    text.setOnMousePressed(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) { // Used for dragging
        dragX = floorPane.getTranslateX() - event.getSceneX();
        dragY = floorPane.getTranslateY() - event.getSceneY();
      }
    });

    text.setOnMouseDragged(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        if (checkXWithinBounds(event.getSceneX())) {
          floorPane.setTranslateX(event.getSceneX() + dragX);
        }
        if (checkYWithinBounds(event.getSceneY())) {
          floorPane.setTranslateY(event.getSceneY() + dragY);
        }
      }
    });

    textLayer.getChildren().add(pane);
  }

  /**
   * Clears the text layer
   */
  public void clearText() {
    textLayer.getChildren().clear();
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
      this.building = building;

      clearText();
      alignMap((int) backgroundImage.getImage().getWidth() / 2,
          (int) backgroundImage.getImage().getHeight() / 2);

      colorLayer.setMaxWidth(backgroundImage.getBoundsInLocal().getWidth() - 12);
      colorLayer.setMaxHeight(backgroundImage.getBoundsInLocal().getHeight() - 8);
      draw();
    } catch (Exception e) {
      log.error("The floor map associated with that floor doesn't exist!");
    }
  }

  /**
   * Aligns the map to the given location
   *
   * @param x the x coordinate (in image coords)
   * @param y the y coordinate (in image coords)
   */
  public void alignMap(int x, int y) {
    // Figure out what the middle coordinates are with (0,0) in the top left corner
    double centerX = (backgroundImage.getBoundsInLocal().getWidth() / 2);
    double centerY = (backgroundImage.getBoundsInLocal().getHeight() / 2);

    // Based on the center of the map as (0,0), figure out the translations for a set of coordinates
    floorPane.setTranslateX(translateToPaneX(x) - centerX);
    floorPane.setTranslateY(translateToPaneY(y) - centerY);
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
  }

  /**
   * Find a NodeLine that an edge is represented by
   *
   * @param n1 node 1's ID
   * @param n2 node 2's ID
   * @return the NodeLine that node refers to (or null if no NodeLine is found)
   */
  private NodeLine findLine(String n1, String n2) {
    for (val child : edgeGroup.getChildren()) {
      val nodeLine = (NodeLine) child;
      if ((nodeLine.node1.getNodeID().equals(n1) &&
          nodeLine.node2.getNodeID().equals(n2)) ||
          (nodeLine.node1.getNodeID().equals(n2) &&
            nodeLine.node2.getNodeID().equals(n1))) {
        return nodeLine;
      }
    }
    return null;
  }

  private int translateToImageX(int x) {
    return (int) (x / getRealWidth() * backgroundImage.getImage().getWidth());
  }

  private int translateToImageY(int y) {
    return (int) (y / getRealHeight() * backgroundImage.getImage().getHeight());
  }

  private int translateToPaneX(int x) {
    return (int) (x / backgroundImage.getImage().getWidth() * getRealWidth());
  }

  private int translateToPaneY(int y) {
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

      this.node1 = node1;
      this.node2 = node2;
    }
  }

  /**
   * A NodeCoordConsumer
   */
  @FunctionalInterface
  public interface NodeCoordConsumer {

    void accept(Node node, int x, int y);
  }

  public void onEvent(Event event) {
    if (event.getClass().equals(FloorSwitchEvent.class)) {
      setFloor(((FloorSwitchEvent) event).getBuilding(), ((FloorSwitchEvent) event).getFloor());
      dispatch(new RedrawEvent());
    }
  }
}