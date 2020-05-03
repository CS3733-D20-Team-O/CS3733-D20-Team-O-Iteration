package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FloorSelector extends ViewModelBase {

  // todo translate
  private NodeMapView nodeMapViewController;
  @FXML
  private JFXButton floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn;

  /**
   * Sets the nodeMapViewController
   *
   * @param controller The controller
   */
  public void setNodeMapViewController(NodeMapView controller) {
    nodeMapViewController = controller;
  }

  /**
   * Sets the floor of the application
   */
  @FXML
  private void setFloor(ActionEvent event) {
    JFXButton target = (JFXButton) event.getSource();
    int floor = Integer.parseInt(target.getText());
    for (JFXButton btn : Arrays.asList(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn)) {
      btn.setStyle("-fx-background-color: lightgray; -fx-background-radius: 30;");
    }
    target.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    nodeMapViewController.setFloor(floor);
  }

}