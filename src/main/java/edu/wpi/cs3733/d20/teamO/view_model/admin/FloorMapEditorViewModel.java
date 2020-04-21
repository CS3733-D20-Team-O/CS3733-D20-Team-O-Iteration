package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.TableProperty;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
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
import lombok.val;

public class FloorMapEditorViewModel extends ViewModelBase implements DatabaseWrapper {
  @FXML private NodeMapView nodeMapViewController;
  @FXML private JFXTextField nodeXField;
  @FXML private JFXTextField nodeYField;
  @FXML private JFXTextField nodeIDField;
  @FXML private AnchorPane sideBar;
  @FXML private Label floorLabel;
  @FXML private VBox addNodePane;
  @FXML private VBox addEdgePane;
  @FXML private ChoiceBox nodeType;
  @FXML private JFXTextField shortNameField;
  @FXML private JFXTextField longNameField;
  @FXML private JFXTextField node1Field;
  @FXML private JFXTextField node2Field;

  private Map<String, Node> nodeMap = new HashMap<>();
  private Node nodeSelection1;
  private Node nodeSelection2;

  @Override
  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  protected void start(URL location, ResourceBundle resources) {
    super.start(location, resources);
    nodeMapViewController.setNodeMap(nodeMap);
    nodeType.getItems().addAll("STAI","ELEV","REST","DEPT","LABS","HALL","CONF");
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
    // todo write method
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
   * @param actionEvent
   */
  public void addEdge(ActionEvent actionEvent) {
    clearSideBar();
    showSideBar();
    addEdgePane.setVisible(true);
  }

  /**
   * Called when the add node button is pressed
   * @param actionEvent
   */
  public void addNode(ActionEvent actionEvent) {
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
      Node node = new Node(id,x,y,nodeMapViewController.getFloor(),"Faulkner",type,longName,shortName);
      nodeMap.put(id, node);
      nodeMapViewController.setNodeMap(nodeMap);
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
    // todo add edge in NodeMapView
  }

  /**
   * Called when the cancel button is clicked in the add edge menu
   * @param actionEvent
   */
  public void cancelAddEdge(ActionEvent actionEvent) {
    clearSideBar();
  }

  public void exportNodes(ActionEvent actionEvent) {
  }

  public void exportEdges(ActionEvent actionEvent) {
  }

  public void deleteSelectedNode(ActionEvent actionEvent) {
  }

  public void cancelNodeSelection(ActionEvent actionEvent) {
  }

  public void clearEdge1Selection(ActionEvent actionEvent) {
    nodeSelection1 = null;
    node1Field.clear();
  }

  public void clearEdge2Selection(ActionEvent actionEvent) {
    nodeSelection2 = null;
    node2Field.clear();
  }

  @Override
  public int addNode(String nodeID, int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName) {
    return 0;
  }

  @Override
  public int addEdge(String edgeID, String startNodeID, String stopNodeID) {
    return 0;
  }

  @Override
  public int addServiceRequest(String requestID, String requestTime, String requestNode,
      String type, String requesterName, String whoMarked, String employeeAssigned) {
    return 0;
  }

  @Override
  public int addEmployee(String employeeID, String name, String type, boolean isAvailable) {
    return 0;
  }

  @Override
  public int deleteFromTable(Table table, TableProperty property, String matching) {
    return 0;
  }

  @Override
  public int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
      String data) {
    return 0;
  }

  @Override
  public int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
      int data) {
    return 0;
  }

  @Override
  public Map<String, Node> exportNodes() {
    return null;
  }

  @Override
  public List<Edge> exportEdges() {
    return null;
  }

  @Override
  public List<ServiceRequest> exportServiceRequests() {
    return null;
  }

  @Override
  public List<Employee> exportEmployees() {
    return null;
  }

  @Override
  public String employeeNameFromID(String id) {
    return null;
  }
  // todo add listeners
}
