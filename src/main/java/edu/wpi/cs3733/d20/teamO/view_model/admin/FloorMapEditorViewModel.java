package edu.wpi.cs3733.d20.teamO.view_model.admin;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FloorMapEditorViewModel extends ViewModelBase {
  private Map<String, Node> nodeMap = new HashMap<>();
  private int floor = 1; // the current floor
  @FXML private NodeMapView nodeMapView;
  public void addEdge(ActionEvent actionEvent) {
    // todo add edge between two nodes given
  }

  public void addNode(ActionEvent actionEvent) {
    // todo get info from user to fill in
    int x = 1;
    int y = 1;
    String name = "name";
    nodeMap.put(name, new Node(name, x, y, floor, "", "", "", ""));
    nodeMapView.setNodeMap(nodeMap);
  }

  public void floorDown(ActionEvent actionEvent) {
    // todo lower floor number
  }

  public void floorUp(ActionEvent actionEvent) {
    // todo raise floor number
  }
}
