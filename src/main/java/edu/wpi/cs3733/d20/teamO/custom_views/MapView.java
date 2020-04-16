package edu.wpi.cs3733.d20.teamO.custom_views;

import edu.wpi.cs3733.d20.teamO.model.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

@Getter
@NoArgsConstructor
public class MapView extends Region implements Initializable {

  /**
   * The current floor being displayed
   */
  private final int floor = 1;
  /**
   * Gets called when a node is clicked
   */
  @Setter
  private Consumer<Node> onNodeTappedListener;

  /**
   * The node map to use to fuel the display
   */
  private Map<String, Node> nodeMap = new HashMap<>();
  /**
   * Gets called when a click on the map occurs but it is not on a node
   */
  @Setter
  private BiConsumer<Integer, Integer> onMissTapListener;

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * @param location  The location used to resolve relative paths for the root object, or {@code
   *                  null} if the location is not known.
   * @param resources The resources used to localize the root object, or {@code null} if
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // todo draw floor and set up basic stuff
  }

  public void setNodeMap(Map<String, Node> nodeMap) {
    this.nodeMap = nodeMap;
    // Clear so we can redraw the new nodes
    clear();
    // Draw all the new nodes
    drawAll();
  }

  private void drawAll() {
    nodeMap.keySet().forEach((id) -> {
      val node = nodeMap.get(id);
      if (node.getFloor() == floor) {
        drawNode(node);
      }
    });
  }

  private void drawNode(Node node) {
    // todo draw this node
  }

  private void clear() {
    // todo clear the canvas
  }

  public void drawEdge(Node n1, Node n2) {
    // draw edge between two nodes
    // in case line goes over nodes, simply call drawNode() for both nodes after drawing line
  }

  /*
  image view for floor
  canvas for nodes and edges
  listen for an on tap in the canvas
    - fire either onNodeTappedListener if a node was tapped or the other one
    - calculate distance from finger to node via Math.hypot
    - go through the nodeMap and find the closest node (if map isnt empty)
    - and if the closest node is within the radius + ~10 pixels of each drawn node
    - call on node tapped, otherwise call the other listener with the coordinates
  when switch floors clear canvas and redraw
   */
}
