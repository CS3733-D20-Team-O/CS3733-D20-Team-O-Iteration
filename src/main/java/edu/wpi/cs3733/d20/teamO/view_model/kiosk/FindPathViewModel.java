package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.AStar;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  private final AStar path = new AStar();
  private Node beginning;
  private Node finish;
  private int clicks = 0;

  @FXML
  Label prompt;
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
    nodeMap = new HashMap<>();
    Node a = new Node("thing", 700, 400, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    Node b = new Node("thing", 459, 540, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    Node c = new Node("thing", 348, 492, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    Node d = new Node("thing", 300, 987, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    Node e = new Node("thing", 339, 400, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    Node f = new Node("thing", 308, 434, 1, "ehif", "odjfoi","fjoiej", "iejfio");
    a.neighbors.add(b);
    a.neighbors.add(f);
    b.neighbors.add(a);
    f.neighbors.add(a);
    c.neighbors.add(f);
    f.neighbors.add(c);
    d.neighbors.add(b);
    b.neighbors.add(d);
    d.neighbors.add(e);
    e.neighbors.add(f);
    nodeMap.put("fish", a);
    nodeMap.put("ejfo", b);
    nodeMap.put("fei", c);
    nodeMap.put("fejhi", d);
    nodeMap.put("kjejfi", e);
    nodeMap.put("ekfi", f);

    nodeMapViewController.setNodeMap(nodeMap);
    nodeMapViewController.setOnNodeTappedListener(node -> {
      prompt.setText("It works");
      switch (clicks){
        case 0:
          beginning = node;
          prompt.setText("Press Ending Location");
          break;
        case 1:
          finish = node;
          List<Node> nodes = path.findPathBetween(beginning,finish);
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

  @FXML
  void resetPath(){
  prompt.setText("Press Starting Point");
  clicks = 0;

  }

}
