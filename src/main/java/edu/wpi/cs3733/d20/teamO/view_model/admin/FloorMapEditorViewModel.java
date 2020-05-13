package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.RedrawEvent;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject}) // required for database
public class FloorMapEditorViewModel extends ViewModelBase {

  private enum State {
    MAIN, SELECT_NODE, SELECT_EDGE, ADD_NODE, ADD_EDGE, ADD_NEIGHBOR, DRAGGING, SELECT_NODES, SELECT_EDGES
  }

  // FXML fields
  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private VBox nodeSelectView, edgeSelectView, addNodeView, addEdgeView, addNeighborView, sideBar, selectNodesView;
  @FXML
  private BorderPane root;
  @FXML
  private Label floorLabel, nodeCategoryLabel, selectedNeighborLabel, newEdgeStartNode, edgeNode1ID, edgeNode2ID;
  @FXML
  private JFXTextField shortNameField, longNameField, newNodeShortNameField, newNodeLongNameField;
  @FXML
  private JFXListView neighboringNodesList, selectedNodesList;
  @FXML
  private AnchorPane clipper;
  @FXML
  private JFXComboBox newNodeCategory;
  @FXML
  private JFXButton saveNodeChangesBtn;
  private Map<String, Node> nodeMap;
  private List<Edge> edges;

  // globally accessible fields
  private State state; // current state of view
  private final LinkedList<Node> selection = new LinkedList<>();
  private Node selectedNode, selectedTargetNode, previewNode; // todo move over to selection
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
    JFXDepthManager.setDepth(sideBar, 2);
    newNodeCategory.getItems()
        .addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "SERV", "CONF", "HALL");
    // set the state
    setState(State.MAIN);
    // grabbing data from the database
    exportDatabase();
    nodeMapViewController.setNodeMap(nodeMap);
    // drawing the map
    Platform.runLater(this::redraw);
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
    nodeMapViewController.setOnNodeLeftDragListener((node, x, y) -> { // node dragged
      if (state == State.SELECT_NODE
          || state == State.DRAGGING) { // only can drag a node if the node is selected
        setState(State.DRAGGING);
        database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
            NodeProperty.X_COORD, Integer.toString(x));
        database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
            NodeProperty.Y_COORD, Integer.toString(y));
        exportDatabase();
        val newNode = nodeMap.get(node.getNodeID());
        nodeMapViewController.updateNode(node, newNode);
      }
    });
    nodeMapViewController.setOnEdgeLeftTapListener((node1, node2) -> {
      selectEdge(findEdge(node1, node2));
    });
  }

  // FXML methods

  public void alignNodesVertical(ActionEvent event) {
    alignNodes('v');
  }

  public void alignNodesHorizontal(ActionEvent event) {
    alignNodes('h');
  }

  public void markNodeChangesMade(KeyEvent keyEvent) {
    saveNodeChangesBtn.setDisable(false);
  }

  public void cancelPressed(ActionEvent actionEvent) {
    switch (state) {
      case ADD_NEIGHBOR:
        setState(State.SELECT_NODE);
        if (selectedTargetNode != null) {
          nodeMapViewController.unhighlightNode(selectedTargetNode);
          selectedTargetNode = null;
        }
        break;
      default:
        setState(State.MAIN);
    }
  }

  public void addNeighborPressed(ActionEvent actionEvent) {
    switch (state) {
      case SELECT_NODE:
        selectedTargetNode = null;
        selectedNeighborLabel.setText("");
        setState(State.ADD_NEIGHBOR);
        break;
      case ADD_NEIGHBOR:
        boolean success = createEdge(selectedNode, selectedTargetNode);
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
        setState(State.MAIN);
        break;
      case SELECT_NODES:
        deleteNodes(selection);
        break;
    }
    setState(State.MAIN);
  }

  public void saveNodeChangesPressed(ActionEvent actionEvent) {
    val valid = validator.validate(shortNameField, longNameField);
    if (valid) {
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.LONG_NAME, longNameField.getText()); // update long name
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, selectedNode.getNodeID(),
          NodeProperty.SHORT_NAME, shortNameField.getText()); // update short name
      exportDatabase();
      redraw();
      snackBar.show("Node updated");
      setState(State.MAIN);
    }
  }

  public void createEdgePressed(ActionEvent actionEvent) {
    boolean success = createEdge(selectedNode, selectedTargetNode);
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
    clearFields();
    previewNode = new Node("", x, y, nodeMapViewController.getFloor(),
        nodeMapViewController.getBuilding(), "", "", "");
    nodeMapViewController.addNode(previewNode);
    nodeMapViewController.highlightNode(previewNode);
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
    if (node.equals(previewNode)) {
      return;
    }
    clearFields();
    selectedNode = node;
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
    if (node.equals(previewNode)) {
      return;
    }
    switch (state) {
      case ADD_NEIGHBOR:
        if (selectedTargetNode != null) {
          nodeMapViewController.unhighlightNode(selectedTargetNode);
        }
        selectedTargetNode = node;
        nodeMapViewController.highlightNode(node);
        selectedNeighborLabel.setText(node.getLongName());
        break;
      case ADD_EDGE:
        boolean success = createEdge(selectedNode, node);
        if (success) {
          setState(State.MAIN);
        }
        break;
      case SELECT_NODE:
        if (!selectedNode.getNodeID().equals(node.getNodeID())) {
          setState(State.SELECT_NODES);
        } else {
          nodeMapViewController.unhighlightNode(node);
          setState(State.MAIN);
          break;
        }
      case SELECT_NODES:
        if (selection.contains(node)) {
          selection.remove(node);
          selectedNodesList.getItems().remove(node.getLongName());
          nodeMapViewController.unhighlightNode(node);
          if (selection.size() == 1) {
            node = selectedNode;
          } else {
            break;
          }
        } else {
          selection.add(node);
          nodeMapViewController.highlightNode(node);
          selectedNodesList.getItems().add(node.getLongName());
          break;
        }
      default:
        clearFields();
        setState(State.SELECT_NODE);
        selectedNode = node;
        nodeMapViewController.highlightNode(node);
        shortNameField.setText(node.getShortName());
        longNameField.setText(node.getLongName());
        nodeCategoryLabel.setText(node.getNodeType());
        for (val neighbor : node.getNeighbors()) {
          neighboringNodesList.getItems().add(neighbor.getLongName());
        }
        selection.add(node);
        selectedNodesList.getItems().add(node.getLongName());
        break;
    }
  }

  /**
   * Deletes a given node
   * @param node The node to delete
   */
  private void deleteNode(Node node) {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, node.getNodeID());
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
   * Deletes a set of nodes
   *
   * @param nodes The list of nodes
   */
  private void deleteNodes(LinkedList<Node> nodes) {
    for (Node node : nodes) {
      database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, node.getNodeID());
    }
    exportDatabase();
    redraw();
    snackBar.show("Nodes deleted");
    setState(State.MAIN);
  }

  /**
   * Selects an edge
   *
   * @param edge The edge to select
   */
  private void selectEdge(Edge edge) {
    if (edge.equals(selectedEdge)) {
      return; // same edge is selected
    }
    setState(State.SELECT_EDGE);
    if (selectedEdge != null) {
      nodeMapViewController.unhighlightEdge(selectedEdge);
    }
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
    Stream.of(nodeCategoryLabel, selectedNeighborLabel, newEdgeStartNode,
        edgeNode1ID, edgeNode2ID).forEach(node -> node.setText(""));
    Stream.of(neighboringNodesList, selectedNodesList).forEach(node -> node.getItems().clear());
    for (Node node : selection) {
      nodeMapViewController.unhighlightNode(node);
    }
    saveNodeChangesBtn.setDisable(true);
    selection.clear();
    if (selectedNode != null) {
      nodeMapViewController.unhighlightNode(selectedNode);
      selectedNode = null;
    }
    if (selectedTargetNode != null) {
      nodeMapViewController.unhighlightNode(selectedTargetNode);
      selectedTargetNode = null;
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
    database.addNode(x, y, nodeMapViewController.getFloor(), nodeMapViewController.getBuilding(),
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
    Stream.of(nodeSelectView, edgeSelectView, addNodeView, addEdgeView, addNeighborView,
        selectNodesView).forEach(view -> view.setVisible(false));
    switch (state) {
      case MAIN:
        clearFields();
        break;
      case SELECT_EDGE:
        clearFields();
        edgeSelectView.setVisible(true);
        break;
      case SELECT_NODE:
        if (selectedTargetNode != null) {
          nodeMapViewController.unhighlightNode(selectedTargetNode);
          selectedTargetNode = null;
        }
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
        break;
      case SELECT_NODES:
        selectNodesView.setVisible(true);
        break;
      case DRAGGING:
        nodeSelectView.setVisible(true);
        break;
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

  /**
   * Aligns nodes in a straight line
   *
   * @param orientation The orientation of the nodes: 'v' for vertical and 'h' for horizontal
   */
  private void alignNodes(char orientation) {
    NodeProperty property = orientation == 'v' ? NodeProperty.X_COORD
        : NodeProperty.Y_COORD; // gets the proper node property to update in the database
    int coord = orientation == 'v' ? selection.get(0).getXCoord()
        : selection.get(0).getYCoord(); // gets the value of the coord to update
    for (Node node : selection) {
      database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, node.getNodeID(),
          property, coord);
    }
    exportDatabase();
    redraw();
    for (Node node : selection) { // re-highlights the nodes
      nodeMapViewController.highlightNode(node);
    }
  }

  public void onEvent(Event event) {
    if (event.getClass().equals(RedrawEvent.class)) {
      drawEdges();
    }
  }
}

