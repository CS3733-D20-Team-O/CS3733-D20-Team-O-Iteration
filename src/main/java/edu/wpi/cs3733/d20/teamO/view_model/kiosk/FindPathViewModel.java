package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.AStar;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
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

  @FXML
  Label prompt;
  @FXML
  NodeMapView nodeMapViewController;
  @FXML
  Button reset;

  @Override
/**
 * Called when a ViewModel's views have been completely processed and can be used freely
 *
 * @param location  the location used to resolve relative paths for the root object, or null
 * @param resources the resources used to localize the root object, or null
 */
  protected void start(URL location, ResourceBundle resources) {
    super.start(location, resources);
    nodeMapViewController.setOnNodeTappedListener(node -> {

    });
    nodeMapViewController.setOnMissTapListener((x, y) -> {

    });
  }

  @FXML
  void resetPath(){
  prompt.setText("Press Starting Point");
  }

}
