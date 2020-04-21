package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
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

public class FloorMapEditorViewModel extends ViewModelBase {
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

  Node selection;
  private Map<String, Node> nodeMap = new HashMap<>();

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
      selection = node;
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
      updateCoords(x, y);
    });
  }

  private void updateCoords(int x, int y) {
    System.out.println("Selected "+x+", "+y);
    nodeXField.setText(Integer.toString(x));
    nodeYField.setText(Integer.toString(y));
  }

  /**
   * Called when the add edge button is pressed
   * @param actionEvent
   */
  public void addEdge(ActionEvent actionEvent) {
    addNodePane.setVisible(false);
    addEdgePane.setVisible(true);
  }

  /**
   * Called when the add node button is pressed
   * @param actionEvent
   */
  public void addNode(ActionEvent actionEvent) {
    nodeXField.clear();
    nodeYField.clear();
    nodeIDField.clear();
    shortNameField.clear();
    longNameField.clear();
    nodeType.getSelectionModel().clearSelection();
    addNodePane.setVisible(true);
    addEdgePane.setVisible(false);
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
    addNodePane.setVisible(false);
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
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Invalid input");
      alert.setContentText("The input you entered was invalid.");
      alert.showAndWait();
    }
    addNodePane.setVisible(false);
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
    addEdgePane.setVisible(false);
  }

  public void exportNodes(ActionEvent actionEvent) {
  }

  public void exportEdges(ActionEvent actionEvent) {
  }
  // todo add listeners
}
