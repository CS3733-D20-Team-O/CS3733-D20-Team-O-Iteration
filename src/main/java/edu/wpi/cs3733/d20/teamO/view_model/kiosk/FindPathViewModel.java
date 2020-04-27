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
    DRAWN
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
    nodeMapViewController.setOnNodeTappedListener(node -> {

      switch (currentState) {
        case START:
          beginning = node;
          currentState = State.END;
          break;
        case END:
//          if (node.equals(beginning)) {
//            currentState = State.START;
//            break;
//          }
          finish = node;
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
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    currentState = State.START;
    beginning = null;
    finish = null;
    nodeMapViewController.draw();
  }

  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    if (currentState == State.DRAWN) {
      drawPath();
    }
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    if (currentState == State.DRAWN) {
      drawPath();
    }
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
}
