package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.path_finding.AStar;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private Node beginning;
  private Node finish;
  private int clicks = 0;

  @FXML
  Label prompt, start, end;

  @FXML
  NodeMapView nodeMapViewController;

  Map<String, Node> nodeMap;

  @Override
/**
 * Called when a ViewModel's views have been completely processed and can be used freely
 * @param location  the location used to resolve relative paths for the root object, or null
 * @param resources the resources used to localize the root object, or null
 */
  protected void start(URL location, ResourceBundle resources) {
    nodeMap = database.exportNodes();
    nodeMapViewController.setNodeMap(nodeMap);
    nodeMapViewController.setOnNodeTappedListener(node -> {
      prompt.setText("Follow path");
      switch (clicks) {
        case 0:
          beginning = node;
          prompt.setText("Press Ending Point");
          start.setText(node.getLongName());
          break;
        case 1:
          if (node.equals(beginning)) {
            prompt.setText("Invalid Location Try Again");
            clicks = 0;
            break;
          }
          finish = node;
          end.setText(node.getLongName());
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
      prompt.setText("Please Click a Possible Location");
    });
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    prompt.setText("Press Starting Point");
    start.setText("");
    end.setText("");
    clicks = 0;
    nodeMapViewController.draw();
  }

}
