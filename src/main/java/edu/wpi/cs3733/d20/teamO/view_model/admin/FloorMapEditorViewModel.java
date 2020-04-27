package edu.wpi.cs3733.d20.teamO.view_model.admin;

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
    edgeSelectView.setVisible(false);
    nodeSelectView.setVisible(false);
    state = State.MAIN;
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
    // todo implement
  }

  public void removeNeighborPressed(ActionEvent actionEvent) {
    // todo bug checking
    if (neighboringNodesList.getSelectionModel().getSelectedItem() == null) {
      snackBar.show("No neighbor selected");
      return;
    }
    Node node = selectedNode.getNeighbors().get(neighboringNodesList.getSelectionModel()
        .getSelectedIndex()); // gets the selected node by using the index in the list
    Edge edge = findEdge(selectedNode, node); // finds the edge between the two nodes
    deleteEdge(edge); // deletes the edge
    neighboringNodesList.getItems().clear();
    for (val neighbor : node.getNeighbors()) { // refresh the neighboring nodes list
      neighboringNodesList.getItems().add(neighbor.getLongName());
    }
    drawEdges();
    snackBar.show(node.getLongName() + " removed as neighbor");
  }

  public void deletePressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        deleteNode(selectedNode);
        nodeSelectView.setVisible(false);
        break;
      case SELECT_EDGE:
        deleteEdge(selectedEdge);
        edgeSelectView.setVisible(false);
        break;
    }
    state = State.MAIN;
  }

  public void saveChangesPressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        val valid = validator.validate(shortNameField, longNameField);
        if (valid) {
          // todo save all changes to local node and to database
          nodeSelectView.setVisible(false);
          state = State.MAIN;
        }
        break;
      case SELECT_EDGE:
        // todo implement
        break;
    }
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
  private VBox nodeSelectView, edgeSelectView;
  @FXML
  private BorderPane root;
  @FXML
  private Label floorLabel, nodeCategoryLabel;
  @FXML
  private JFXTextField shortNameField, longNameField;
  @FXML
  private JFXSlider zoomSlider;
  @FXML
  private JFXListView neighboringNodesList;
  private Map<String, Node> nodeMap;
  private List<Edge> edges;
  private final DatabaseWrapper database;
  private Node selectedNode;
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
    nodeSelectView.setVisible(false);
    edgeSelectView.setVisible(false);
    /*
    nodeCategoryPicker.getItems()
        .addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "SERV", "CONF", "HALL");
    nodeCategoryPicker.setPromptText("Choose");

     */
    // grabbing data from the database
    nodeMap = database.exportNodes();
    edges = database.exportEdges();
    // drawing the map
    nodeMapViewController.setNodeMap(nodeMap);
    drawEdges();
    // set the state
    state = State.MAIN;
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
   *
   * @param node The node to select
   */
  private void selectNode(Node node) {
    switch (state) {
      case SELECT_EDGE:
        edgeSelectView.setVisible(false);
      default:
        nodeSelectView.setVisible(true);
        state = State.SELECT_NODE;
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
   *
   * @param node The node to delete
   */
  private void deleteNode(Node node) {
    nodeMap.remove(selectedNode.getNodeID());
    nodeMapViewController.deleteNode(selectedNode);
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID());
    // have to reload edges from database in case an edge is deleted in the process and then redraw
    edges = database.exportEdges();
    drawEdges();
    snackBar.show("Node deleted");
  }

  /**
   * Deletes a given edge
   *
   * @param edge The edge to delete
   */
  private void deleteEdge(Edge edge) {
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, edge.getEdgeID());
    edges = database.exportEdges();
    nodeMap = database.exportNodes();
    nodeMapViewController.setNodeMap(nodeMap);
    drawEdges();
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
    snackBar.show("Edge not found");
    return null;
  }
}

/*
 *//**
 * Called when the create node button is pressed in the add node menu
 *
 * @param actionEvent
 * <p>
 * Creates an edge between two selected nodes
 * @param actionEvent
 *//*
  public void createNode(ActionEvent actionEvent) {
    // fix this piece of garbage
    if (nodeIDField.getText().isBlank() || nodeXField.getText().isBlank() || nodeYField.getText()
        .isBlank()
        || shortNameField.getText().isBlank() || longNameField.getText().isBlank()
        || nodeType.getValue() == null) {
      showErrorSnackbar("You cannot leave any fields blank");
      return;
    }
    if (nodeMap.get(nodeIDField.getText()) != null) {
      showErrorSnackbar("A node with that ID already exists");
      return;
    }
    try {
      val x = Integer.parseInt(nodeXField.getText());
      val y = Integer.parseInt(nodeYField.getText());
      val id = nodeIDField.getText();
      val shortName = shortNameField.getText();
      val longName = longNameField.getText();
      val type = nodeType.getSelectionModel().getSelectedItem().toString();
      Node node = new Node(id, x, y, nodeMapViewController.getFloor(), "Faulkner", type, longName,
          shortName);
      nodeMapViewController.addNode(node);
      drawEdges();
      nodeMap.put(node.getNodeID(), node);
      database.addNode(id, x, y, nodeMapViewController.getFloor(), "Faulkner", type, longName,
          shortName); //adds node to database
      clearFields(false);
    } catch (Exception e) {
      showErrorSnackbar("You must enter numbers for the coordinates");
      return;
    }
  }

  *//**
   * Creates an edge between two selected nodes
   * @param actionEvent
   *//*
  public void createEdge(ActionEvent actionEvent) throws Exception {
    // also fix this piece of garbage
    try {
      val node1ID = node1Field.getText();
      val node2ID = node2Field.getText();
      val edgeID = edgeIDField.getText();
      if (node1ID.isBlank() || node2ID.isBlank() || edgeID.isBlank()) {
        showErrorSnackbar("You cannot leave any fields blank");
        return;
      }
      for (val edge : edges) {
        if (edge.getEdgeID().equals(edgeID)) {
          showErrorSnackbar("An edge with that ID already exists");
          return;
        }
      }
      nodeSelection1 = nodeMap.get(node1ID);
      nodeSelection2 = nodeMap.get(node2ID);
      if (!nodeSelection1.equals(nodeSelection2)) {
        if (nodeSelection1.getNeighbors().contains(nodeSelection2)) {
          showErrorSnackbar("An edge already exists between these two nodes");
          return;
        }
        Edge newEdge = new Edge(edgeID, node1ID, node2ID);
        database.addEdge(edgeID, node1ID, node2ID);
        edges.add(newEdge);
        nodeSelection1.getNeighbors().add(nodeSelection2);
        nodeSelection2.getNeighbors().add(nodeSelection1);
        nodeMapViewController.drawEdge(nodeSelection1, nodeSelection2);
        clearFields(false);
      } else {
        showErrorSnackbar("Cannot make a path between the same node");
      }
    } catch (Exception e) {
      showErrorSnackbar("Invalid input");
    }
    */