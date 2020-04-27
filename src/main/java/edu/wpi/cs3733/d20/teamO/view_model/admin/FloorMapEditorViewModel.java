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
    nodeMapViewController.decrementFloor();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawEdges();
  }

  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawEdges();
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
        setState(State.ADD_NEIGHBOR);
        break;
      case ADD_NEIGHBOR:
        if (neighborSelection == null) {
          snackBar.show("No node selected");
          return;
        }
        createEdge(selectedNode, neighborSelection);
        setState(State.SELECT_NODE);
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
    snackBar.show(node.getLongName() + " removed as neighbor");
    neighboringNodesList.getItems().clear();
    // todo refresh list
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

  public void saveChangesPressed(ActionEvent actionEvent) {
    val valid = validator.validate(shortNameField, longNameField);
    if (valid) {
      if (shortNameField.getText().equals(selectedNode.getShortName()) && longNameField.getText()
          .equals(selectedNode.getLongName())) { // no changes made
        setState(State.MAIN);
        return;
      }
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.LONG_NAME, longNameField.getText()); // update long name
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.SHORT_NAME, shortNameField.getText()); // update short name
      exportDatabase();
      setState(State.MAIN);
      snackBar.show("Node updated");
    }
  }

  public void createEdgePressed(ActionEvent actionEvent) {
    // todo implement
  }

  private enum State {
    MAIN, SELECT_NODE, SELECT_EDGE, ADD_NODE, ADD_EDGE, ADD_NEIGHBOR
  }

  private State state; // current state of view
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
  private JFXTextField shortNameField, longNameField;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private JFXListView neighboringNodesList;
  @FXML
  private JFXComboBox newNodeCategory;
  private Map<String, Node> nodeMap;
  private List<Edge> edges;
  private final DatabaseWrapper database;
  private Node selectedNode, edgeTarget, neighborSelection;
  private Edge selectedEdge;
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
      // todo implement
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
        neighborSelection = node;
        selectedNeighborLabel.setText(node.getLongName());
        break;
      case ADD_EDGE:
        if (edgeTarget == null) { // no target node has been selected
          edgeTarget = node;
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
    setState(State.MAIN);
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
    Stream.of(shortNameField, longNameField).forEach(node -> node.clear());
    neighboringNodesList.getItems().clear();
    // todo clear validator
  }

  /**
   * Creates a new node
   *
   * @param x The x-coord of the node
   * @param y The y-coord of the node
   */
  private void createNode(int x, int y) {
    // todo implement
  }

  /**
   * Adds an edge between two nodes
   *
   * @param node1 The source node
   * @param node2 The dest node
   */
  private void createEdge(Node node1, Node node2) {
    String id = "placeholder";
    node1.getNeighbors().add(node2);
    node2.getNeighbors().add(node1);
    database.addEdge(id, node1.getNodeID(), node2.getNodeID());
    exportDatabase();
    drawEdges();
  }

  /**
   * Finds the edge for two connected nodes
   *
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
   *
   * @param state The state to set
   */
  private void setState(State state) {
    if (this.state == state) {
      return; // if the state is not changing then do nothing
    }
    switch (state) {
      case MAIN:
        clearFields();
        nodeSelectView.setVisible(false);
        edgeSelectView.setVisible(false);
        addNodeView.setVisible(false);
        addEdgeView.setVisible(false);
        break;
      case SELECT_EDGE:
        nodeSelectView.setVisible(false);
        edgeSelectView.setVisible(true);
        break;
      case SELECT_NODE:
        edgeSelectView.setVisible(false);
        nodeSelectView.setVisible(true);
        addNeighborView.setVisible(false);
        break;
      case ADD_NODE:
        nodeSelectView.setVisible(false);
        edgeSelectView.setVisible(false);
        addNodeView.setVisible(true);
        break;
      case ADD_EDGE:
        nodeSelectView.setVisible(false);
        edgeSelectView.setVisible(false);
        addEdgeView.setVisible(true);
        break;
      case ADD_NEIGHBOR:
        nodeSelectView.setVisible(false);
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
   * Gets a list of the long names of the neighbors to a node
   *
   * @return
   */
  private String[] getNeighborsList() {
    // todo implement
    return null;
  }

  /**
   * Redraws all nodes and edges
   */
  private void redraw() {
    nodeMapViewController.setNodeMap(nodeMap);
    drawEdges();
  }
}