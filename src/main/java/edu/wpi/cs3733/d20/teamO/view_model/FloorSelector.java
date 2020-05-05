package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.events.BuildingSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FloorSelector extends ViewModelBase {

  @FXML
  private JFXButton floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn, faulknerBtn, mainCampusBtn;

  /**
   * Sets the floor of the application
   */
  @FXML
  private void setFloor(ActionEvent event) {
    JFXButton target = (JFXButton) event.getSource();
    String floor = target.getText();
    for (JFXButton btn : Arrays
        .asList(floor1Btn, floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn)) {
      btn.setStyle("-fx-background-color: lightgray; -fx-background-radius: 30;");
    }
    target.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    dispatch(new FloorSwitchEvent(floor));
  }

  /**
   * Sets the building
   */
  public void setBuilding(ActionEvent event) {
    JFXButton target = (JFXButton) event.getSource();
    String building = target.getText();
    switch (building) {
      case ("Faulkner"):
        Stream.of(floor5Btn).forEach(btn -> {
          btn.setDisable(false);
        });
        Stream.of(L1Btn, L2Btn, GBtn).forEach(btn -> {
          btn.setDisable(true);
        });
        break;
      case ("Main Campus"):
        Stream.of(floor5Btn).forEach(btn -> {
          btn.setDisable(true);
        });
        Stream.of(L1Btn, L2Btn, GBtn).forEach(btn -> {
          btn.setDisable(false);
        });
        break;
    }
    for (JFXButton btn : Arrays
        .asList(floor2Btn, floor3Btn, floor4Btn, floor5Btn, L1Btn, L2Btn, GBtn, faulknerBtn,
            mainCampusBtn)) {
      btn.setStyle("-fx-background-color: lightgray; -fx-background-radius: 30;");
    }
    target.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    floor1Btn.setStyle("-fx-background-color: lightseagreen; -fx-background-radius: 30;");
    dispatch(new BuildingSwitchEvent(building));
  }
}