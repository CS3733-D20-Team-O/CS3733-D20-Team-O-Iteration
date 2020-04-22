package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.AStar;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  private final AStar path = new AStar();
  private final DatabaseWrapper database;
  private Node beginning;
  private Node finish;
  private int clicks = 0;

  @FXML
  Label prompt;
  @FXML
  Label start;
  @FXML
  Label end;
  @FXML
  NodeMapView nodeMapViewController;
  @FXML
  Button reset;

  Map<String, Node> nodeMap;

  @Override
/**
 * Called when a ViewModel's views have been completely processed and can be used freely
 * @param location  the location used to resolve relative paths for the root object, or null
 * @param resources the resources used to localize the root object, or null
 */
  protected void start(URL location, ResourceBundle resources) {
    super.start(location, resources);
    nodeMap = database.exportNodes();
    nodeMapViewController.setNodeMap(nodeMap);
    nodeMapViewController.setOnNodeTappedListener(node -> {
      prompt.setText("Follow path");
      switch (clicks){
        case 0:
          beginning = node;
          prompt.setText("Press Ending Location");
          start.setText(node.getLongName());
          break;
        case 1:
          finish = node;
          end.setText(node.getLongName());
          List<Node> nodes = AStar.findPathBetween(beginning, finish);
          for (int i = 0; i <= nodes.size() - 1; i++){
            if(i != nodes.size() - 1){
              nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
            }
          }
          break;
        default:
          break;
      }
      clicks++;
    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {
      prompt.setText("Please Click a Room");
    });
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    prompt.setText("Press Starting Point");
    clicks = 0;

  }

}
