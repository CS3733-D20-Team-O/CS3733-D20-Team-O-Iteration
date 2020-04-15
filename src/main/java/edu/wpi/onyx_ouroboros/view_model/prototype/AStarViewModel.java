package edu.wpi.onyx_ouroboros.view_model.prototype;

// drawing circles and lines to represent nodes and edges

import com.jfoenix.controls.JFXTextField;
import edu.wpi.onyx_ouroboros.model.astar.old.MapNode;
import edu.wpi.onyx_ouroboros.model.astar.old.MapSearcher;
import edu.wpi.onyx_ouroboros.model.astar.old.Parser;
import edu.wpi.onyx_ouroboros.model.astar.old.Path;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class AStarViewModel extends ViewModelBase {
  @FXML private Canvas canvas;
  @FXML private Canvas pathCanvas;
  @FXML private TextField node1Field;
  @FXML private TextField node2Field;

  private double nodeRadius = 30; // the radius of each node
  private int width = 5; // the number of nodes in x
  private int height = 6; // the number of nodes in y
  private boolean first = true; // bool indicating whether the user is currently selecting the first or second node
  private MapSearcher pathFinder = new MapSearcher();
  private Parser parser = new Parser();
  private double nodeDistX;
  private double nodeDistY;
  private HashMap<String, MapNode> nodes = new HashMap<String, MapNode>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    parser.create();
    nodeDistX = canvas.getWidth() / width;
    nodeDistY = canvas.getHeight() / height;
    generateMap();
  }

  /**
   * Generates the map of nodes
   */
  private void generateMap() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        drawNode(x, y);
      }
    }
    // now fill the hashmap with values
    nodes.put("a", parser.getNode("a"));
    nodes.put("b", parser.getNode("b"));
    nodes.put("c", parser.getNode("c"));
    nodes.put("d", parser.getNode("d"));
    nodes.put("e", parser.getNode("e"));
    nodes.put("f", parser.getNode("f"));
    nodes.put("g", parser.getNode("g"));
    nodes.put("h", parser.getNode("h"));

    // and draw the node letters
    drawNodeText(nodes.get("a").getX(), nodes.get("a").getY(), "a");
    drawNodeText(nodes.get("b").getX(), nodes.get("b").getY(), "b");
    drawNodeText(nodes.get("c").getX(), nodes.get("c").getY(), "c");
    drawNodeText(nodes.get("d").getX(), nodes.get("d").getY(), "d");
    drawNodeText(nodes.get("e").getX(), nodes.get("e").getY(), "e");
    drawNodeText(nodes.get("f").getX(), nodes.get("f").getY(), "f");
    drawNodeText(nodes.get("g").getX(), nodes.get("g").getY(), "g");
    drawNodeText(nodes.get("h").getX(), nodes.get("h").getY(), "h");

  }

  /**
   * Draws a node
   * @param x The x-coord of the node
   * @param y The y-coord of the node
   */
  private void drawNode(int x, int y) {
    GraphicsContext context = canvas.getGraphicsContext2D();
    context.setFill(Color.MAROON);
    context.fillOval(x * nodeDistX, y * nodeDistY, nodeRadius, nodeRadius);
    context.setStroke(Color.BLACK);
    context.strokeOval(x * nodeDistX, y * nodeDistY, nodeRadius, nodeRadius);
  }

  /**
   * Draws node text
   * @param x The x-coord of the node
   * @param y The y-coord of the node
   * @param text The node text
   */
  private void drawNodeText(int x, int y, String text) {
    GraphicsContext context = canvas.getGraphicsContext2D();
    context.setFill(Color.BLACK);
    y = height - y; // adjusting for the coordinate system
    context.setTextAlign(TextAlignment.CENTER);
    context.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    context.fillText(text.toUpperCase(), x * nodeDistX + 15, y * nodeDistY - 38);
  }

  /**
   * Called when the 'Generate Path' button is pressed
   * @param event The ActionEvent
   */
  @FXML
  private void generatePath(ActionEvent event) {
    System.out.println("Generating path...");
    String node1 = node1Field.getText();
    String node2 = node2Field.getText();
    node1Field.clear();
    node2Field.clear();
    pathFinder.setStart(parser.getNode(node1));
    pathFinder.setStop(parser.getNode(node2));
    Path path = pathFinder.AStar();
    LinkedList<MapNode> nodes = path.getNodes();
    GraphicsContext context = pathCanvas.getGraphicsContext2D();
    context.clearRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());
    for (int i = 0; i < nodes.size() - 1; i++) {
      drawPath(nodes.get(i), nodes.get(i + 1));
    }
  }

  /**
   * Draws the path between two nodes on the map
   * @param first The first node selected
   * @param second The second node selected
   */
  private void drawPath(MapNode first, MapNode second) {
    GraphicsContext context = pathCanvas.getGraphicsContext2D();
    context.setStroke(Color.BLACK);
    context.setLineWidth(4);
    int x1 = first.getX();
    int y1 = height - first.getY();
    int x2 = second.getX();
    int y2 = height - second.getY();
    context.strokeLine(x1 * nodeDistX + nodeRadius / 2, y1 * nodeDistY - nodeRadius * 1.5, x2 * nodeDistX + nodeRadius / 2, y2 * nodeDistY - nodeRadius * 1.5);
  }
}
