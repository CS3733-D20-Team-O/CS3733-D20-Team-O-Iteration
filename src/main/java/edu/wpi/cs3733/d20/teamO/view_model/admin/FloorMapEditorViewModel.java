package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject}) // required for database
public class FloorMapEditorViewModel extends ViewModelBase {

  // todo add translations
  // todo prevent commas from being added
  // todo add validator for elevator names

  private enum State {
    MAIN, SELECT_NODE, SELECT_EDGE, ADD_NODE, ADD_EDGE, ADD_NEIGHBOR
  }

  // FXML fields
  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private VBox nodeSelectView, edgeSelectView, addNodeView, addEdgeView, addNeighborView, sideBar;
  @FXML
  private BorderPane root;
  @FXML
  private Label floorLabel, nodeCategoryLabel, selectedNeighborLabel, newEdgeStartNode, newEdgeDestNode, edgeNode1ID, edgeNode2ID;
  @FXML
  private JFXTextField shortNameField, longNameField, newNodeShortNameField, newNodeLongNameField;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private JFXListView neighboringNodesList;
  @FXML
  private AnchorPane clipper;
  @FXML
  private JFXComboBox newNodeCategory;
  private Map<String, Node> nodeMap;
  private List<Edge> edges;

  // globally accessible fields
  private State state; // current state of view
  private Node selectedNode1, selectedNode2, previewNode;
  private Edge selectedEdge;
  private int xSelection, ySelection; // the selected x and y coords

  // misc tools
  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @Override
  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  protected void start(URL location, ResourceBundle resources) {
    // styling the UI components
    val clipRect = new Rectangle();
    clipRect.widthProperty().bind(clipper.widthProperty());
    clipRect.heightProperty().bind(clipper.heightProperty());
    clipper.setClip(clipRect);
    zoomSlider.setValue(100);
    JFXDepthManager.setDepth(sideBar, 2);
    newNodeCategory.getItems()
        .addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "SERV", "CONF", "HALL");
    // set the state
    setState(State.MAIN);
    // grabbing data from the database
    exportDatabase();
    nodeMapViewController.setNodeMap(nodeMap);
    // drawing the map
    redraw();
    // configure listeners
    nodeMapViewController.setOnNodeLeftTapListener(node -> { // node left-clicked
      selectNode(node);
    });
    nodeMapViewController.setOnMissRightTapListener((x, y) -> { // map right-clicked
      addNodeView(x, y);
    });
    nodeMapViewController.setOnNodeRightTapListener(node -> { // node right-clicked
      addEdgeView(node);
    });
    nodeMapViewController.setOnNodeLeftDragListener((node, mouseEvent) -> { // node dragged
      if (state == State.SELECT_NODE) { // only can drag a node if the node is selected
        int adjX = nodeMapViewController.translateToImageX((int) mouseEvent.getX());
        int adjY = nodeMapViewController.translateToImageY((int) mouseEvent.getY());
        nodeMapViewController.relocateNode(node, adjX, adjY);
        database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode1.getNodeID(),
            NodeProperty.X_COORD, Integer.toString(adjX));
        database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode1.getNodeID(),
            NodeProperty.Y_COORD, Integer.toString(adjY));
      }
    });
    nodeMapViewController
        .setOnNodeLeftDragReleaseListener((node, mouseEvent) -> { // node drag released
          if (state == State.SELECT_NODE) {
            System.out.println("Node relocated");
          }
        });
    nodeMapViewController.setOnEdgeLeftTapListener((node1, node2) -> {
      selectEdge(findEdge(node1, node2));
    });
  }

  // FXML methods

  public void setZoom() {
    nodeMapViewController.zoom(zoomSlider.getValue() / 100);
  }

  public void cancelPressed(ActionEvent actionEvent) {
    switch (state) {
      case ADD_NEIGHBOR:
        setState(State.SELECT_NODE);
        if (selectedNode2 != null) {
          nodeMapViewController.unhighlightNode(selectedNode2);
          selectedNode2 = null;
        }
        break;
      default:
        setState(State.MAIN);
    }
  }

  public void floorDownPressed(ActionEvent actionEvent) {
    if (state != State.ADD_NODE) {
      nodeMapViewController.decrementFloor();
      floorLabel.setText("Floor " + nodeMapViewController.getFloor());
      drawEdges();
    }
  }

  public void floorUpPressed(ActionEvent actionEvent) {
    if (true) {
      nodeMapViewController.incrementFloor();
      floorLabel.setText("Floor " + nodeMapViewController.getFloor());
      drawEdges();
    }
  }

  public void zoomOutPressed(ActionEvent actionEvent) {
    zoomSlider.decrement();
    setZoom();
  }

  public void zoomInPressed(ActionEvent actionEvent) {
    zoomSlider.increment();
    setZoom();
  }

  public void addNeighborPressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        selectedNode2 = null;
        selectedNeighborLabel.setText("");
        setState(State.ADD_NEIGHBOR);
        break;
      case ADD_NEIGHBOR:
        boolean success = createEdge(selectedNode1, selectedNode2);
        if (success) {
          neighboringNodesList.getItems().add(selectedNode1.getLongName());
          setState(State.SELECT_NODE);
        }
    }
  }

  public void removeNeighborPressed(ActionEvent actionEvent) {
    if (neighboringNodesList.getSelectionModel().getSelectedItem() == null) {
      snackBar.show("No neighbor selected");
      return;
    }
    Node node = selectedNode1.getNeighbors().get(neighboringNodesList.getSelectionModel()
        .getSelectedIndex()); // gets the selected node by using the index in the list
    Edge edge = findEdge(selectedNode1, node); // finds the edge between the two nodes
    deleteEdge(edge); // deletes the edge
    neighboringNodesList.getItems()
        .remove(neighboringNodesList.getSelectionModel().getSelectedIndex());
    snackBar.show(node.getLongName() + " removed as neighbor");
  }

  public void deletePressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        deleteNode(selectedNode1);
        break;
      case SELECT_EDGE:
        deleteEdge(selectedEdge);
        break;
    }
    setState(State.MAIN);
  }

  public void saveNodeChangesPressed(ActionEvent actionEvent) {
    val valid = validator.validate(shortNameField, longNameField);
    if (valid) {
      if (shortNameField.getText().equals(selectedNode1.getShortName()) && longNameField.getText()
          .equals(selectedNode1.getLongName())) { // no changes made
        return;
      }
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode1.getNodeID(),
          NodeProperty.LONG_NAME, longNameField.getText()); // update long name
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode1.getNodeID(),
          NodeProperty.SHORT_NAME, shortNameField.getText()); // update short name
      exportDatabase();
      redraw();
      snackBar.show("Node updated");
    }
  }

  public void createEdgePressed(ActionEvent actionEvent) {
    boolean success = createEdge(selectedNode1, selectedNode2);
    if (success) {
      setState(State.MAIN);
    }
  }

  public void createNodePressed(ActionEvent actionEvent) {
    val valid = validator.validate(newNodeLongNameField, newNodeShortNameField, newNodeCategory);
    if (valid) {
      boolean success = createNode(xSelection, ySelection,
          newNodeCategory.getSelectionModel().getSelectedItem().toString(),
          newNodeLongNameField.getText(), newNodeShortNameField.getText());
      if (success) {
        setState(State.MAIN);
      }
    }
  }

  // Non-FXML methods

  /**
   * Opens the menu for creating a new node
   *
   * @param x The x-coordinate of the selection
   * @param y The y-coordinate of the selection
   */
  private void addNodeView(int x, int y) {
    if (state == State.ADD_NEIGHBOR || state == State.ADD_NODE || state == State.ADD_EDGE) {
      return;
    }
    clearFields();
    previewNode = new Node("", x, y, nodeMapViewController.getFloor(), "Faulkner", "", "", "");
    nodeMapViewController.addNode(previewNode);
    xSelection = x;
    ySelection = y;
    setState(State.ADD_NODE);
  }

  /**
   * Opens the menu for creating a new edge
   *
   * @param node The first selected node
   */
  private void addEdgeView(Node node) {
    if (state == State.ADD_NEIGHBOR || state == State.ADD_NODE || state == State.ADD_EDGE) {
      return;
    }
    clearFields();
    selectedNode1 = node;
    nodeMapViewController.highlightNode(node);
    newEdgeStartNode.setText(node.getLongName());
    setState(State.ADD_EDGE);
  }

  /**
   * Redraws all edges on the node map
   */
  private void drawEdges() {
    for (val edge : edges) {
      nodeMapViewController.drawEdge(nodeMap.get(edge.getStartID()), nodeMap.get(edge.getStopID()));
    }
  }

  /**
   * Selects a given node
   * @param node The node to select
   */
  private void selectNode(Node node) {
    switch (state) {
      case ADD_NEIGHBOR:
        if (selectedNode2 != null) {
          nodeMapViewController.unhighlightNode(selectedNode2);
        }
        selectedNode2 = node;
        nodeMapViewController.highlightNode(node);
        selectedNeighborLabel.setText(node.getLongName());
        break;
      case ADD_EDGE:
        if (selectedNode2 != null) {
          nodeMapViewController.unhighlightNode(selectedNode2);
        }
        selectedNode2 = node;
        nodeMapViewController.highlightNode(node);
        newEdgeDestNode.setText(node.getLongName());
        break;
      case ADD_NODE: // cannot select node in this state
        break;
      default:
        clearFields();
        setState(State.SELECT_NODE);
        selectedNode1 = node;
        nodeMapViewController.highlightNode(node);
        shortNameField.setText(node.getShortName());
        longNameField.setText(node.getLongName());
        nodeCategoryLabel.setText(node.getNodeType());
        for (val neighbor : node.getNeighbors()) {
          neighboringNodesList.getItems().add(neighbor.getLongName());
        }
        break;
    }
  }

  /**
   * Deletes a given node
   * @param node The node to delete
   */
  private void deleteNode(Node node) {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode1.getNodeID());
    exportDatabase();
    redraw();
    snackBar.show("Node deleted");
    setState(State.MAIN);
  }

  /**
   * Deletes a given edge
   *
   * @param edge The edge to delete
   */
  private void deleteEdge(Edge edge) {
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, edge.getEdgeID());
    exportDatabase();
    redraw();
    snackBar.show("Edge deleted");
  }

  /**
   * Selects an edge
   *
   * @param edge The edge to select
   */
  private void selectEdge(Edge edge) {
    if (state == State.ADD_NEIGHBOR || state == State.ADD_NODE || state == State.ADD_EDGE) {
      return;
    }
    setState(State.SELECT_EDGE);
    nodeMapViewController.highlightEdge(edge);
    selectedEdge = edge;
    edgeNode1ID.setText(nodeMap.get(edge.getStartID()).getLongName());
    edgeNode2ID.setText(nodeMap.get(edge.getStopID()).getLongName());
  }

  /**
   * Clears all fields in the program and clears node selections
   */
  private void clearFields() {
    Stream.of(shortNameField, longNameField, newNodeShortNameField, newNodeLongNameField)
        .forEach(node -> node.clear());
    Stream.of(shortNameField, longNameField, newNodeShortNameField, newNodeLongNameField,
        newNodeCategory)
        .forEach(node -> node.resetValidation());
    Stream.of(nodeCategoryLabel, selectedNeighborLabel, newEdgeStartNode, newEdgeDestNode,
        edgeNode1ID, edgeNode2ID).forEach(node -> node.setText(""));
    newNodeCategory.getSelectionModel().clearSelection();
    neighboringNodesList.getItems().clear();
    if (selectedNode1 != null) {
      nodeMapViewController.unhighlightNode(selectedNode1);
      selectedNode1 = null;
    }
    if (selectedNode2 != null) {
      nodeMapViewController.unhighlightNode(selectedNode2);
      selectedNode2 = null;
    }
    if (previewNode != null) {
      nodeMapViewController.deleteNode(previewNode);
      previewNode = null;
    }
    if (selectedEdge != null) {
      nodeMapViewController.unhighlightEdge(selectedEdge);
      selectedEdge = null;
    }
  }

  /**
   * Creates a new node
   *
   * @param x         The x-position of the node
   * @param y         The y-position of the node
   * @param nodeType  The type of the node
   * @param longName  The short name of the node
   * @param shortName The short name of the node
   * @return True if the node was successfully created, false otherwise
   */
  private boolean createNode(int x, int y, String nodeType, String longName, String shortName) {
    database.addNode(x, y, nodeMapViewController.getFloor(), "Faulkner",
        nodeType, longName, shortName);
    exportDatabase();
    redraw();
    snackBar.show("Node created successfully");
    return true;
  }

  /**
   * Adds an edge between two nodes
   *
   * @param node1 The source node
   * @param node2 The dest node
   * @return True if the edge was created successfully, false otherwise
   */
  private boolean createEdge(Node node1, Node node2) {
    if (node1 == null || node2 == null) {
      snackBar.show("No node was selected");
      return false;
    }
    if (node1.equals(node2)) {
      snackBar.show("Cannot add an edge between a node and itself");
      return false;
    }
    if (node1.getNeighbors().contains(node2)) {
      snackBar.show("An edge already exists between these two nodes");
      return false;
    }
    node1.getNeighbors().add(node2);
    node2.getNeighbors().add(node1);
    database.addEdge(node1.getNodeID(), node2.getNodeID());
    exportDatabase();
    drawEdges();
    snackBar.show("Edge created successfully");
    return true;
  }

  /**
   * Finds the edge for two connected nodes
   * @param node1 The first node
   * @param node2 The second node
   * @return The edge, or null is no edge is found
   */
  private Edge findEdge(Node node1, Node node2) {
    String node1ID = node1.getNodeID(), node2ID = node2.getNodeID();
    for (val edge : edges) {
      String start = edge.getStartID(), stop = edge.getStopID(); // start and stop node ids
      if (node1ID.equals(start) && node2ID.equals(stop)) {
        return edge;
      }
      if (node2ID.equals(start) && node1ID.equals(stop)) {
        return edge;
      }
    }
    return null;
  }

  /**
   * Sets the state of the application
   * @param state The state to set
   */
  private void setState(State state) {
    if (this.state == state) {
      return; // if the state is not changing then do nothing
    }
    nodeSelectView.setVisible(false);
    edgeSelectView.setVisible(false);
    addNodeView.setVisible(false);
    addEdgeView.setVisible(false);
    addNeighborView.setVisible(false);
    switch (state) {
      case MAIN:
        clearFields();
        break;
      case SELECT_EDGE:
        edgeSelectView.setVisible(true);
        break;
      case SELECT_NODE:
        nodeSelectView.setVisible(true);
        break;
      case ADD_NODE:
        addNodeView.setVisible(true);
        break;
      case ADD_EDGE:
        addEdgeView.setVisible(true);
        break;
      case ADD_NEIGHBOR:
        addNeighborView.setVisible(true);
    }
    this.state = state;
  }

  /**
   * Exports the data from the database to the nodeMap and edges fields
   */
  private void exportDatabase() {
    nodeMap = database.exportNodes();
    edges = database.exportEdges();
  }

  /**
   * Redraws all nodes and edges
   */
  private void redraw() {
    nodeMapViewController.setNodeMap(nodeMap);
    drawEdges();
  }
}