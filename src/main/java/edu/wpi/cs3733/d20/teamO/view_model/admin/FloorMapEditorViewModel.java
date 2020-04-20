package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class FloorMapEditorViewModel extends ViewModelBase {
  private Map<String, Node> nodeMap = new HashMap<>();
  private int floor = 1; // the current floor
  private int maxFloor = 5; // the highest floor on the map
  //@FXML private NodeMapView nodeMapView;
  @FXML private JFXTextField nodeXField;
  @FXML private JFXTextField nodeYField;
  @FXML private JFXTextField nodeIDField;
  @FXML private AnchorPane addNodePane;
  @FXML private Label floorLabel;

  /**
   * Called when the add edge button is pressed
   * @param actionEvent The ActionEvent
   */
  public void addEdge(ActionEvent actionEvent) {
    // todo add edge between two nodes given
  }

  /**
   * Called when the add node button is pressed
   * @param actionEvent The ActionEvent
   */
  public void addNode(ActionEvent actionEvent) {
    addNodePane.setVisible(true);
  }

  /**
   * Called when the floor down button is pressed
   * @param actionEvent The ActionEvent
   */
  public void floorDown(ActionEvent actionEvent) {
    // todo increment floor
  }

  /**
   * Called when the floor up button is pressed
   * @param actionEvent The ActionEvent
   */
  public void floorUp(ActionEvent actionEvent) {
    // todo decrement floor
  }

  /**
   * Called when the cancel button is pressed for adding a node
   * @param actionEvent The ActionEvent
   */
  public void cancelAddNode(ActionEvent actionEvent) {
    addNodePane.setVisible(false);
  }

  /**
   * Called when the create node button is pressed in the add node menu
   * @param actionEvent The ActionEvent
   */
  public void createNode(ActionEvent actionEvent) {
    int x = Integer.parseInt(nodeXField.getText());
    int y = Integer.parseInt(nodeYField.getText());
    String name = nodeIDField.getText();
    addNodePane.setVisible(false);
    // todo add node in NodeMapView
    nodeXField.clear();
    nodeYField.clear();
    nodeIDField.clear();
  }
}
