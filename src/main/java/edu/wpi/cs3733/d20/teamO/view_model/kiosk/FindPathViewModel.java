package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
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
    END
  }

  int clicks = 0;

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

  Map<String, Node> nodeMap;
  private final DatabaseWrapper database;
  Node beginning;
  Node finish;

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

      switch (clicks) {
        case 0:
          beginning = node;
          break;
        case 1:
          if (node.equals(beginning)) {
            clicks = 0;
            break;
          }
          finish = node;
          List<Node> nodes = AStar.findPathBetween(beginning, finish);
          for (int i = 0; i < nodes.size() - 1; i++) {
              nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
          }
          break;
        default:
          break;
      }
      clicks++;
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
    });
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    clicks = 0;
    nodeMapViewController.draw();
  }

  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
  }

  @FXML
  public void zoomOutPressed() {

  }

  @FXML
  public void zoomInPressed() {

  }

}
