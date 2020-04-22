package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FloorMapEditorViewModel extends ViewModelBase {

  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private JFXTextField nodeXField;
  @FXML
  private JFXTextField nodeYField;
  @FXML
  private JFXTextField nodeIDField;
  @FXML
  private AnchorPane sideBar;
  @FXML
  private Label floorLabel;
  @FXML
  private VBox addNodePane;
  @FXML
  private VBox addEdgePane;
  @FXML
  private ChoiceBox nodeType;
  @FXML
  private JFXTextField shortNameField;
  @FXML
  private JFXTextField longNameField;
  @FXML
  private JFXTextField node1Field;
  @FXML
  private JFXTextField node2Field;
  @FXML
  private VBox exportNodesPane;
  @FXML
  private VBox exportEdgesPane;
  @FXML
  private JFXTextField edgeIDField;

  private Map<String, Node> nodeMap = new HashMap<>();
  private Node nodeSelection1, nodeSelection2;
  private List<Edge> edges = new LinkedList<>();
  private final DatabaseWrapper database; //todo implement this

  @Override
  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  protected void start(URL location, ResourceBundle resources) {
    super.start(location, resources);
    nodeMap.putAll(database.exportNodes());
    edges.addAll(database.exportEdges());
    nodeMapViewController.setNodeMap(nodeMap);
    nodeType.getItems().addAll("STAI", "ELEV", "REST", "DEPT", "LABS", "HALL", "CONF");
    nodeMapViewController.setOnNodeTappedListener(node -> {
      select(node);
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
      updateCoords(x, y);
    });
  }

  /**
   * Updates the currently selected node
   * @param node The selected node
   */
  private void select(Node node) {
    if (addEdgePane.isVisible()) { // if the user is currently adding an edge
      if (node1Field.getText().isBlank()) {
        node1Field.setText(node.getNodeID());
        nodeSelection1 = node;
      } else if (node2Field.getText().isBlank()) {
        node2Field.setText(node.getNodeID());
        nodeSelection2 = node;
      }
    } else { // node is selected from elsewhere
      clearSideBar();
      // todo implement node selection menu
    }
  }

  /**
   * Clears all content of the sidebar and empties all text fields
   */
  private void clearSideBar() {
    addNodePane.setVisible(false);
    addEdgePane.setVisible(false);
    nodeXField.clear();
    nodeYField.clear();
    nodeIDField.clear();
    shortNameField.clear();
    longNameField.clear();
    nodeType.getSelectionModel().clearSelection();
    node1Field.clear();
    node2Field.clear();
    edgeIDField.clear();
    exportEdgesPane.setVisible(false);
    exportNodesPane.setVisible(false);
    sideBar.setVisible(false);
    // todo clear all other stuff
  }

  /**
   * Displays the sidebar
   */
  private void showSideBar() {
    sideBar.setVisible(true);
  }

  /**
   * Updates the selected coordinates when a part of the map is clicked (non-node)
   * @param x The x-coordinate of the selection
   * @param y The y-coordinate of the selection
   */
  private void updateCoords(int x, int y) {
    if (addNodePane.isVisible()) {
      nodeXField.setText(Integer.toString(x));
      nodeYField.setText(Integer.toString(y));
    }
  }

  /**
   * Called when the add edge button is pressed
   *
   * @param actionEvent
   */
  public void addEdgeButton(ActionEvent actionEvent) {
    clearSideBar();
    showSideBar();
    addEdgePane.setVisible(true);
  }

  /**
   * Called when the add node button is pressed
   *
   * @param actionEvent
   */
  public void addNodeButton(ActionEvent actionEvent) {
    clearSideBar();
    showSideBar();
    addNodePane.setVisible(true);
  }

  /**
   * Called when the floor down button is pressed
   * @param actionEvent
   */
  public void floorDown(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    floorLabel.setText("Floor "+ nodeMapViewController.getFloor());
  }

  /**
   * Called when the floor up button is pressed
   * @param actionEvent The ActionEvent
   */
  public void floorUp(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    floorLabel.setText("Floor "+ nodeMapViewController.getFloor());
  }

  /**
   * Called when the cancel button is pressed for adding a node
   * @param actionEvent
   */
  public void cancelAddNode(ActionEvent actionEvent) {
    clearSideBar();
  }

  /**
   * Called when the create node button is pressed in the add node menu
   * @param actionEvent
   */
  public void createNode(ActionEvent actionEvent) throws Exception {
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
      nodeMap.put(node.getNodeID(), node);
      database.addNode(id, x, y, nodeMapViewController.getFloor(), "Faulkner", type, longName,
          shortName); //adds node to database
      clearSideBar();
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Invalid input");
      alert.setContentText("The input you entered was invalid.");
      alert.showAndWait();
    }
  }

  /**
   * Creates an edge between two selected nodes
   * @param actionEvent
   */
  public void createEdge(ActionEvent actionEvent) {
    Edge newEdge = new Edge(edgeIDField.getText(), node1Field.getText(), node2Field.getText());
    database.addEdge(edgeIDField.getText(), node1Field.getText(), node2Field.getText());
    edges.add(newEdge);
    nodeMapViewController.drawEdge(nodeSelection1, nodeSelection2);
    clearSideBar();
  }

  /**
   * Called when the cancel button is clicked in the add edge menu
   *
   * @param actionEvent
   */
  public void cancelAddEdge(ActionEvent actionEvent) {
    clearSideBar();
  }

  public void exportNodesButton(ActionEvent actionEvent) {
    clearSideBar();
    showSideBar();
    exportNodesPane.setVisible(true);
  }

  public void exportEdgesButton(ActionEvent actionEvent) {
    clearSideBar();
    showSideBar();
    exportEdgesPane.setVisible(true);
  }

  public void deleteSelectedNode(ActionEvent actionEvent) {
  }

  public void cancelNodeSelection(ActionEvent actionEvent) {
    clearSideBar();
  }

  public void clearEdge1Selection(ActionEvent actionEvent) {
    nodeSelection1 = null;
    node1Field.clear();
  }

  public void clearEdge2Selection(ActionEvent actionEvent) {
    nodeSelection2 = null;
    node2Field.clear();
  }
}
