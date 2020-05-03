package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FloorSelector extends ViewModelBase {

  // todo translate
  private NodeMapView nodeMapViewController;
  @FXML
  private JFXButton floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn;
  private JFXButton[] buttons = {floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn};

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
   *
   * @param floor The floor to set
   */
  private void setFloor(int floor) {
    nodeMapViewController.setFloor(floor);
    for (int i = 0; i < buttons.length; i++) {
      if (i + 1 == floor) {
        buttons[i].setDisable(true);
      } else {
        buttons[i].setDisable(false);
      }
    }
  }

  public void floor1Select(ActionEvent event) {
    setFloor(1);
  }

  public void floor2Select(ActionEvent event) {
    setFloor(2);
  }

  public void floor3Select(ActionEvent event) {
    setFloor(3);
  }

  public void floor4Select(ActionEvent event) {
    setFloor(4);
  }

  public void floor5Select(ActionEvent event) {
    setFloor(5);
  }
}