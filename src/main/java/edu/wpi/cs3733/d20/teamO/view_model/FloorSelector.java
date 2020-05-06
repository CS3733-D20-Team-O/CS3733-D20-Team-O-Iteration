package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.val;

public class FloorSelector extends ViewModelBase {

  @FXML
  private JFXButton floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn, faulknerBtn, mainCampusBtn, streetBtn;
  private String floor = "1";
  private String building = "Faulkner";

  /**
   * Sets the floor of the application
   */
  @FXML
  private void setFloor(ActionEvent event) {
    val target = (JFXButton) event.getSource();
    floor = target.getText();
    for (JFXButton btn : Arrays
        .asList(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn)) {
      btn.setStyle("-fx-background-color: lightgray; -fx-background-radius: 30;");
    }
    target.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    dispatch(new FloorSwitchEvent(floor, building));
  }

  /**
   * Sets the building
   */
  public void setBuilding(ActionEvent event) {
    val target = (JFXButton) event.getSource();
    building = target.getText();
    switch (building) {
      case ("Faulkner"):
        Stream.of(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn)
            .forEach(btn -> btn.setDisable(false));
        Stream.of(L1Btn, L2Btn, GBtn).forEach(btn -> btn.setDisable(true));
        break;
      case ("Street"):
        Stream.of(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn)
            .forEach(btn -> btn.setDisable(true));
        break;
      case ("Main Campus"):
        floor5Btn.setDisable(true);
        Stream.of(L1Btn, L2Btn, GBtn, floor2Btn, floor3Btn, floor4Btn)
            .forEach(btn -> btn.setDisable(false));
        break;
    }
    for (JFXButton btn : Arrays
        .asList(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn,
            faulknerBtn,
            mainCampusBtn, streetBtn)) {
      btn.setStyle("-fx-background-color: lightgray; -fx-background-radius: 30;");
    }
    target.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    if (!building.equals("Street")) {
      floor1Btn.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    }
    floor = "1";
    dispatch(new FloorSwitchEvent(floor, building));
  }
}