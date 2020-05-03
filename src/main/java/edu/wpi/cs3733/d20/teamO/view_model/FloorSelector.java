package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
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
   * @param nodeMapViewController The controller
   */
  public void setNodeMapViewController(NodeMapView nodeMapViewController) {
    this.nodeMapViewController = nodeMapViewController;
  }

  public void floor1Select(ActionEvent event) {
    nodeMapViewController.setFloor(1);
    floor1Btn.setDisable(true);
    floor2Btn.setDisable(false);
    floor3Btn.setDisable(false);
    floor4Btn.setDisable(false);
    floor5Btn.setDisable(false);
  }

  public void floor2Select(ActionEvent event) {
    nodeMapViewController.setFloor(2);
    floor1Btn.setDisable(false);
    floor2Btn.setDisable(true);
    floor3Btn.setDisable(false);
    floor4Btn.setDisable(false);
    floor5Btn.setDisable(false);
  }

  public void floor3Select(ActionEvent event) {
    nodeMapViewController.setFloor(3);
    floor1Btn.setDisable(false);
    floor2Btn.setDisable(false);
    floor3Btn.setDisable(true);
    floor4Btn.setDisable(false);
    floor5Btn.setDisable(false);
  }

  public void floor4Select(ActionEvent event) {
    nodeMapViewController.setFloor(4);
    floor1Btn.setDisable(false);
    floor2Btn.setDisable(false);
    floor3Btn.setDisable(false);
    floor4Btn.setDisable(true);
    floor5Btn.setDisable(false);
  }

  public void floor5Select(ActionEvent event) {
    nodeMapViewController.setFloor(5);
    floor1Btn.setDisable(false);
    floor2Btn.setDisable(false);
    floor3Btn.setDisable(false);
    floor4Btn.setDisable(false);
    floor5Btn.setDisable(true);
  }
}