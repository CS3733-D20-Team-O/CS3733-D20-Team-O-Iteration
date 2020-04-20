package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
public class NodeMapView extends ViewModelBase {

  final static double nodeSize = 10;

  /**
   * The current floor being displayed
   */
  private SimpleIntegerProperty floor = new SimpleIntegerProperty(1);
  @FXML
  StackPane mapView;
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
   * The node map to use to fuel the display
   */
  private Map<Integer, Map<String, Node>> nodeMap = new HashMap<>();
  //private Map<String, Node> nodeMap = new HashMap<>();
  @FXML
  ImageView backgroundImage;
  @FXML
  Canvas nodeCanvas;
  @FXML
  JFXButton btnIncrementFloor;
  @FXML
  Button btnDecrementFloor;
  @FXML
  Label lblFloor;
  @FXML
  JFXComboBox<Integer> floorList;
  Image floor1 = new Image("backgrounds/Floor1LM.png");
  Image floor2 = new Image("backgrounds/Floor2LM.png");
  Image floor3 = new Image("backgrounds/Floor3LM.png");
  Image floor4 = new Image("backgrounds/Floor4LM.png");
  Image floor5 = new Image("backgrounds/Floor5LM.png");
  /**
   * The set maximum floor
   */
  private int maxFloor = 5;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // todo import floor into ImageView and set up other basic stuff
    backgroundImage = new ImageView(floor1);
    nodeCanvas = new Canvas(backgroundImage.getFitWidth(), backgroundImage.getFitHeight());
    mapView = new StackPane();
    mapView.getChildren().addAll(backgroundImage, nodeCanvas);
    floorList.getItems().add(1);
    floorList.getItems().add(2);
    floorList.getItems().add(3);
    floorList.getItems().add(4);
    floorList.getItems().add(5);

    draw(getFloor());

    // Set up event for when a node is selected (or not selected)
    nodeCanvas.setOnMouseClicked(event -> {
      checkClick((int) event.getX(), (int) event.getY());
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
    this.nodeMap.clear(); // Clear the current node map
    nodeMap.keySet().forEach((id) -> placeFloorNode(id, nodeMap.get(id)));
    draw(getFloor());
  }

  /**
   * Places a node into it's proper floor in the nodeMap
   *
   * @param string a given string for a node
   * @param node   the node to be placed into the nodeMap
   */
  private void placeFloorNode(String string, Node node) {
    if (!this.nodeMap.containsKey(node.getFloor())) {
      nodeMap.put(node.getFloor(), new HashMap<>()); // Make the new floor if it doesn't exist
    }
    nodeMap.get(node.getFloor()).put(string, node);
  }

  /**
   * Clears the canvas and draws all the nodes on a certain floor
   *
   * @param floor given floor to draw
   */
  private void draw(int floor) {
    // Clear the canvas so we can draw fresh
    GraphicsContext nodeGC = nodeCanvas.getGraphicsContext2D();
    nodeGC.clearRect(0, 0, nodeCanvas.getWidth(), nodeCanvas.getHeight());
    // Draw all the nodes on a certain floor
    val floorMap = nodeMap.get(getFloor());
    floorMap.keySet().forEach((id) -> drawNode(floorMap.get(id)));
  }

  /**
   * @param node the node to draw to the canvas
   */
  private void drawNode(Node node) {
    GraphicsContext nodeGC = nodeCanvas.getGraphicsContext2D();
    nodeGC.setFill(Color.RED);
    double canvasX =
        (node.getXCoord() / backgroundImage.getImage().getWidth()) * nodeCanvas.getWidth();
    double canvasY =
        (node.getYCoord() / backgroundImage.getImage().getHeight()) * nodeCanvas.getHeight();
    nodeGC.fillOval(canvasX, canvasY, nodeSize, nodeSize);
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
      double canvasX1 =
          (n1.getXCoord() / backgroundImage.getImage().getWidth()) * nodeCanvas.getWidth();
      double canvasY1 =
          (n1.getYCoord() / backgroundImage.getImage().getHeight()) * nodeCanvas.getHeight();
      double canvasX2 =
          (n2.getXCoord() / backgroundImage.getImage().getWidth()) * nodeCanvas.getWidth();
      double canvasY2 =
          (n2.getYCoord() / backgroundImage.getImage().getHeight()) * nodeCanvas.getHeight();
      nodeGC.strokeLine(canvasX1, canvasY1, canvasX2, canvasY2);

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
    double imageX = (x / nodeCanvas.getWidth()) * backgroundImage.getImage().getWidth();
    double imageY = (y / nodeCanvas.getHeight()) * backgroundImage.getImage().getHeight();

    Map<String, Node> floorMap = nodeMap.get(getFloor());
    floorMap.keySet().forEach((id) -> {
      val node = floorMap.get(id);
      double distance = Math
          .sqrt(Math.pow(imageX - node.getXCoord(), 2) + Math.pow(imageY - node.getYCoord(), 2));
      if (distance <= (nodeSize / 2.0) + 10.0) {
        onNodeTappedListener.accept(node);
      } else {
        onMissTapListener.accept(x, y);
      }
    });
  }

  @FXML
  private void switchFloor() {
    int currentFloor = floorList.getValue();
    if (currentFloor != getFloor() && nodeMap.containsKey(currentFloor)) {
      switch (currentFloor) {
        case 1:
          backgroundImage.setImage(floor1);
          break;
        case 2:
          backgroundImage.setImage(floor2);
          break;
        case 3:
          backgroundImage.setImage(floor3);
          break;
        case 4:
          backgroundImage.setImage(floor4);
          break;
        case 5:
          backgroundImage.setImage(floor5);
          break;
        default:
          // How did you get here?
          break;
      }
      nodeCanvas.setWidth(backgroundImage.getFitWidth());
      nodeCanvas.setHeight(backgroundImage.getFitHeight());
      draw(currentFloor);
      setFloor(currentFloor);
    }
  }

  // Kept this in case we need to switch to a button format
  /*
  // todo you can link this up to the next floor button
  @FXML
  private void incrementFloor() {
    if (getFloor() < maxFloor) {
      val floor = getFloor() + 1;
      // todo update ImageView
      draw(floor);
      setFloor(floor);
    }
  }

  // todo you can link this up to the previous floor button
  @FXML
  private void decrementFloor() {
    if (getFloor() > 1) {
      val floor = getFloor() - 1;
      // todo update ImageView
      draw(floor);
      setFloor(floor);
    }
  }*/

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
