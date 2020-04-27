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
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject}) // required for database
public class FloorMapEditorViewModel extends ViewModelBase {

  // FXML methods

  public void cancelPressed(ActionEvent actionEvent) {
    switch (state) {
      case ADD_NEIGHBOR:
        setState(State.SELECT_NODE);
        break;
      default:
        setState(State.MAIN);
    }
  }

  public void floorDownPressed(ActionEvent actionEvent) {
    if (state == State.MAIN) {
      nodeMapViewController.decrementFloor();
      floorLabel.setText("Floor " + nodeMapViewController.getFloor());
      drawEdges();
    }
  }

  public void floorUpPressed(ActionEvent actionEvent) {
    if (state == State.MAIN) {
      nodeMapViewController.incrementFloor();
      floorLabel.setText("Floor " + nodeMapViewController.getFloor());
      drawEdges();
    }
  }

  public void zoomOutPressed(ActionEvent actionEvent) {
    zoomSlider.decrement();
    // todo implement actual zooming
  }

  public void zoomInPressed(ActionEvent actionEvent) {
    zoomSlider.increment();
    // todo implement actual zooming
  }

  public void addNeighborPressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        selectedNeighbor = null;
        selectedNeighborLabel.setText("");
        setState(State.ADD_NEIGHBOR);
        break;
      case ADD_NEIGHBOR:
        boolean success = createEdge(selectedNode, selectedNeighbor);
        if (success) {
          neighboringNodesList.getItems().add(selectedNode.getLongName());
          setState(State.SELECT_NODE);
        }
    }

  }

  public void removeNeighborPressed(ActionEvent actionEvent) {
    if (neighboringNodesList.getSelectionModel().getSelectedItem() == null) {
      snackBar.show("No neighbor selected");
      return;
    }
    Node node = selectedNode.getNeighbors().get(neighboringNodesList.getSelectionModel()
        .getSelectedIndex()); // gets the selected node by using the index in the list
    Edge edge = findEdge(selectedNode, node); // finds the edge between the two nodes
    deleteEdge(edge); // deletes the edge
    neighboringNodesList.getItems()
        .remove(neighboringNodesList.getSelectionModel().getSelectedIndex());
    snackBar.show(node.getLongName() + " removed as neighbor");
  }

  public void deletePressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        deleteNode(selectedNode);
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
      if (shortNameField.getText().equals(selectedNode.getShortName()) && longNameField.getText()
          .equals(selectedNode.getLongName())) { // no changes made
        return;
      }
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.LONG_NAME, longNameField.getText()); // update long name
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.SHORT_NAME, shortNameField.getText()); // update short name
      exportDatabase();
      redraw();
      snackBar.show("Node updated");
    }
  }

  public void createEdgePressed(ActionEvent actionEvent) {
    // todo implement
  }

  public void createNodePressed(ActionEvent actionEvent) {
    val valid = validator.validate(newNodeLongName, newNodeShortName, newNodeCategory);
    if (valid) {
      boolean success = createNode(xSelection, ySelection,
          newNodeCategory.getSelectionModel().getSelectedItem().toString(),
          newNodeLongName.getText(), newNodeShortName.getText());
      if (success) {
        setState(State.MAIN);
      }
    }
  }

  private enum State {
    MAIN, SELECT_NODE, SELECT_EDGE, ADD_NODE, ADD_EDGE, ADD_NEIGHBOR
  }

  // FXML fields
  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private AnchorPane sideBar;
  @FXML
  private VBox nodeSelectView, edgeSelectView, addNodeView, addEdgeView, addNeighborView;
  @FXML
  private BorderPane root;
  @FXML
  private Label floorLabel, nodeCategoryLabel, selectedNeighborLabel;
  @FXML
  private JFXTextField shortNameField, longNameField, newNodeShortName, newNodeLongName;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private JFXListView neighboringNodesList;
  @FXML
  private JFXComboBox newNodeCategory;
  private Map<String, Node> nodeMap;
  private List<Edge> edges;

  // globally accessible fields
  private State state; // current state of view
  private Node selectedNode, selectedEdgeTarget, selectedNeighbor;
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
    JFXDepthManager.setDepth(sideBar, 2);
    newNodeCategory.getItems()
        .addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "SERV", "CONF", "HALL");
    // grabbing data from the database
    exportDatabase();
    // drawing the map
    redraw();
    // set the state
    setState(State.MAIN);
    // configure listeners
    nodeMapViewController.setOnNodeTappedListener(node -> {
      selectNode(node);
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
      switch (state) {
        // these break cases should not be where nodes can be added
        case ADD_NEIGHBOR:
          break;
        case ADD_NODE:
          break;
        case ADD_EDGE:
          break;
        default:
          // todo change over to right-click listener
          xSelection = x;
          ySelection = y;
          setState(State.ADD_NODE);
      }
    });
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
        selectedNeighbor = node;
        selectedNeighborLabel.setText(node.getLongName());
        break;
      case ADD_EDGE:
        if (selectedEdgeTarget == null) { // no target node has been selected
          selectedEdgeTarget = node;
        }
        break;
      case ADD_NODE: // cannot select node in this state
        break;
      default:
        setState(State.SELECT_NODE);
        selectedNode = node;
        clearFields();
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
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID());
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
    // todo implement
  }

  /**
   * Clears all fields in the program
   */
  private void clearFields() {
    Stream.of(shortNameField, longNameField, newNodeShortName, newNodeLongName)
        .forEach(node -> node.clear());
    newNodeLongName.clear();
    neighboringNodesList.getItems().clear();
    // todo clear validator
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
    // todo get actual node id
    database.addNode("placeholder", x, y, nodeMapViewController.getFloor(), "Faulkner",
        nodeType, longName, shortName);
    exportDatabase();
    redraw();
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
    String id = "placeholder";
    // todo get actual node id
    node1.getNeighbors().add(node2);
    node2.getNeighbors().add(node1);
    database.addEdge(id, node1.getNodeID(), node2.getNodeID());
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
        clearFields();
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