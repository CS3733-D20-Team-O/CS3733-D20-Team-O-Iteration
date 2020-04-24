package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FloorMapEditorViewModel extends ViewModelBase {

  private enum stateEnum {
    MAIN, ADDNODE, ADDEDGE, SELECTNODE, SELECTEDGE
  }

  private stateEnum state;

  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private JFXTextField nodeXField, nodeYField, nodeIDField, shortNameField, longNameField, node1Field, node2Field, edgeIDField;
  @FXML
  private AnchorPane sideBar;
  @FXML
  private VBox addNodePane, selectNodePane, addEdgePane;
  @FXML
  private ChoiceBox nodeType;
  @FXML
  private Label selectedNodeID, selectedNodeX, selectedNodeY, selectedNodeType, selectedNodeShortName, selectedNodeLongName, floorLabel;
  @FXML
  private BorderPane root;
  private Map<String, Node> nodeMap;
  private Node nodeSelection, nodeSelection1, nodeSelection2; // nodeSelection is the node being selected in the normal node selection menu, 1 and 2 are for edge building
  private List<Edge> edges;
  private final DatabaseWrapper database;

  /**
   * Redraws all edges on the node map
   */
  private void drawEdges() {
    for (val edge : edges) {
      nodeMapViewController.drawEdge(nodeMap.get(edge.getStartID()), nodeMap.get(edge.getStopID()));
    }
  }

  @Override
  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  protected void start(URL location, ResourceBundle resources) {
    nodeMap = database.exportNodes();
    edges = database.exportEdges();
    nodeMapViewController.setNodeMap(nodeMap);
    drawEdges();
    nodeType.getItems().addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "HALL", "CONF");
    state = stateEnum.MAIN;
    nodeMapViewController.setOnNodeTappedListener(node -> {
      select(node);
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
      updateCoords(x, y);
    });
  }

  /**
   * Shows an error snackbar
   *
   * @param errorText The text for the snackbar to display
   */
  private void showErrorSnackbar(String errorText) {
    JFXSnackbar bar = new JFXSnackbar(root);
    val label = new Label(errorText);
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    // Add 16 margin and 16 padding as per material design guidelines
    container.setStyle("-fx-background-color: #323232;  -fx-background-insets: 16");
    container.setPadding(new Insets(32)); // total padding, including margin
    bar.enqueue(new SnackbarEvent(container));
  }

  /**
   * Updates the currently selected node
   *
   * @param node The selected node
   */
  private void select(Node node) {
    switch (state) {
      case ADDEDGE:
        if (node1Field.getText().isBlank()) {
          node1Field.setText(node.getNodeID());
          nodeSelection1 = node;
        } else if (node2Field.getText().isBlank()) {
          node2Field.setText(node.getNodeID());
          nodeSelection2 = node;
        }
        break;
      default:
        state = stateEnum.SELECTNODE;
        clearFields(true);
        sideBar.setVisible(true);
        selectNodePane.setVisible(true);
        selectedNodeID.setText("Node ID: " + node.getNodeID());
        selectedNodeLongName.setText("Long name: " + node.getLongName());
        selectedNodeShortName.setText("Short name: " + node.getShortName());
        selectedNodeType.setText("Node type: " + node.getNodeType());
        selectedNodeX.setText("X pos: " + node.getXCoord());
        selectedNodeY.setText("Y pos: " + node.getYCoord());
        nodeSelection = node;
        break;
    }
  }

  /**
   * Clears all content of the sidebar and empties all text fields
   *
   * @param hideMenu Whether or not to hide the side menu
   */
  private void clearFields(boolean hideMenu) {
    if (hideMenu) {
      addNodePane.setVisible(false);
      addEdgePane.setVisible(false);
      selectNodePane.setVisible(false);
      sideBar.setVisible(false);
    }
    // todo put all fields into array to clear at once
    nodeXField.clear();
    nodeYField.clear();
    nodeIDField.clear();
    shortNameField.clear();
    longNameField.clear();
    nodeType.getSelectionModel().clearSelection();
    node1Field.clear();
    node2Field.clear();
    edgeIDField.clear();
  }

  /**
   * Updates the selected coordinates when a part of the map is clicked (non-node)
   * @param x The x-coordinate of the selection
   * @param y The y-coordinate of the selection
   */
  private void updateCoords(int x, int y) {
    nodeXField.setText(Integer.toString(x));
    nodeYField.setText(Integer.toString(y));
  }

  /**
   * Called when the add edge button is pressed
   *
   * @param actionEvent
   */
  public void addEdgePressed(ActionEvent actionEvent) {
    clearFields(true);
    if (state == stateEnum.ADDEDGE) {
      sideBar.setVisible(true);
      addEdgePane.setVisible(true);
      state = stateEnum.MAIN;
    }
  }

  /**
   * Called when the add node button is pressed
   * @param actionEvent
   */
  public void addNodePressed(ActionEvent actionEvent) {
    clearFields(true);
    if (state == stateEnum.ADDEDGE) {
      sideBar.setVisible(true);
      addNodePane.setVisible(true);
      state = stateEnum.MAIN;
    }
  }

  /**
   * Called when the floor down button is pressed
   * @param actionEvent
   */
  public void floorDown(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    drawEdges();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
  }

  /**
   * Called when the floor up button is pressed
   * @param actionEvent The ActionEvent
   */
  public void floorUp(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    drawEdges();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
  }

  /**
   * Called when the cancel button is pressed for adding a node
   * @param actionEvent
   */
  public void cancelAddNode(ActionEvent actionEvent) {
    clearFields(true);
  }

  /**
   * Called when the create node button is pressed in the add node menu
   *
   * @param actionEvent
   */
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

  /**
   * Creates an edge between two selected nodes
   * @param actionEvent
   */
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
  }

  /**
   * Called when the cancel button is clicked in the add edge menu
   *
   * @param actionEvent
   */
  public void cancelAddEdge(ActionEvent actionEvent) {
    clearFields(true);
  }

  /**
   * Deletes the selected node
   *
   * @param actionEvent
   */
  public void deleteSelectedNode(ActionEvent actionEvent) {
    nodeMap.remove(nodeSelection.getNodeID());
    nodeMapViewController.deleteNode(nodeSelection);
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, nodeSelection.getNodeID());
    nodeMapViewController.setNodeMap(nodeMap);
    // have to reload edges from database in case an edge is deleted in the process and then redraw
    edges = database.exportEdges();
    drawEdges();
    clearFields(true);
  }

  /**
   * Called when the cancel button is pressed while a node is selected
   *
   * @param actionEvent
   */
  public void cancelNodeSelection(ActionEvent actionEvent) {
    clearFields(true);
  }

  /**
   * Called when the clear button is pressed for edge 1 selection
   *
   * @param actionEvent
   */
  public void clearEdge1Selection(ActionEvent actionEvent) {
    node1Field.clear();
  }

  /**
   * Called when the clear button is pressed for edge 2 selection
   *
   * @param actionEvent
   */
  public void clearEdge2Selection(ActionEvent actionEvent) {
    node2Field.clear();
  }
}
