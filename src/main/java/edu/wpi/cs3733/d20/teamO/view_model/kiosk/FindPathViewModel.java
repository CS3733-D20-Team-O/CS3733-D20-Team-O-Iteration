package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.AStar;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  enum State {
    START,
    END,
    DRAWN,
    COMBO_START,
    COMBO_STOP
  }

  State currentState = State.START;

  @FXML
  BorderPane root;
  @FXML
  NodeMapView nodeMapViewController;
  @FXML
  AnchorPane sideBar;
  @FXML
  Label floorLabel;
  @FXML
  JFXSlider zoomSlider;

  @FXML
  JFXComboBox startRoom, startFloor, stopRoom, stopFloor;

  Map<String, Node> nodeMap;
  private final DatabaseWrapper database;
  private Node beginning;
  private Node finish;
  private Node defaultStart;
  private Node defaultStop;

  @Override
/**
 * Called when a ViewModel's views have been completely processed and can be used freely
 * @param location  the location used to resolve relative paths for the root object, or null
 * @param resources the resources used to localize the root object, or null
 */
  protected void start(URL location, ResourceBundle resources) {
    JFXDepthManager.setDepth(sideBar, 2);
    nodeMap = database.exportNodes();
    nodeMapViewController.setNodeMap(nodeMap);
    defaultStart = nodeMap.get("AEXIT00101");
    defaultStop = nodeMap.get("AEXIT00101");
    nodeMapViewController.setOnNodeTappedListener(node -> {
      switch (currentState) {
        case START:
          beginning = node;
          startFloor.getSelectionModel().select(node.getFloor() - 1);
          startRoom.getSelectionModel().select(node.getLongName());
          currentState = State.DRAWN;
          break;
        case END:
          finish = node;
          stopFloor.getSelectionModel().select(node.getFloor() - 1);
          stopRoom.getSelectionModel().select(node.getLongName());
          drawPath();
          currentState = State.DRAWN;
          break;
        default:
          break;
      }
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
    });

    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(startFloor.getItems()::add);
    // Set up the populating of locations on each floor
    startFloor.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      startRoom.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(startRoom.getItems()::add);
      startRoom.getSelectionModel().select(0);
    });
    // Preselect the first floor and the first location on that floor
    if (!startFloor.getItems().isEmpty()) {
      startFloor.getSelectionModel().select(0);
      startRoom.getSelectionModel().select(0);
    }

    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(stopFloor.getItems()::add);
    // Set up the populating of locations on each floor
    stopFloor.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      stopRoom.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(stopRoom.getItems()::add);
      stopRoom.getSelectionModel().select(0);
    });
    // Preselect the first floor and the first location on that floor
    if (!stopFloor.getItems().isEmpty()) {
      stopFloor.getSelectionModel().select(0);
      stopRoom.getSelectionModel().select(0);
    }

    resetPath();

  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    beginning = defaultStart;
    finish = defaultStop;
    startFloor.getSelectionModel().select(defaultStart.getFloor() - 1);
    startRoom.getSelectionModel().select(defaultStart.getLongName());
    stopFloor.getSelectionModel().select(defaultStop.getFloor() - 1);
    stopRoom.getSelectionModel().select(defaultStop.getLongName());
    nodeMapViewController.draw();
  }

  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawPath();
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawPath();
  }

  @FXML
  public void zoomOutPressed() {

  }

  @FXML
  public void zoomInPressed() {

  }

  private void drawPath() {
    List<Node> nodes = AStar.findPathBetween(beginning, finish);
    assert nodes != null;
    for (int i = 0; i < nodes.size() - 1; i++) {
      nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
    }
  }

  /**
   * sets beginning on combo box change
   */
  @FXML
  private void onStartCombo() {
    currentState = State.COMBO_START;
    comboState();
  }

  /**
   * sets finish on combo box change
   */
  @FXML
  private void onStopCombo() {
    currentState = State.COMBO_STOP;
    comboState();
  }

  @FXML
  private void setStart() {
    currentState = State.START;
  }

  @FXML
  private void setEnd() {
    currentState = State.END;
  }

  /**
   * sets beginning or finish based on combo box input
   */
  private void comboState() {
    switch (currentState) {
      case COMBO_START:
        for (Node n : nodeMap.values()) {
          if (n.getLongName().equals(startRoom.getSelectionModel().getSelectedItem().toString())) {
            beginning = n;
            break;
          }
        }
        nodeMapViewController.draw();
        drawPath();
        break;
      case COMBO_STOP:
        for (Node n : nodeMap.values()) {
          if (n.getLongName().equals(stopRoom.getSelectionModel().getSelectedItem().toString())) {
            finish = n;
            break;
          }
        }
        nodeMapViewController.draw();
        drawPath();
        break;
      default:
        break;
    }
  }
}
