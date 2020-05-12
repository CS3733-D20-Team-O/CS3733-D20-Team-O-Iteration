package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.events.FloorSwitchEvent;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import lombok.val;

public class FloorSelector extends ViewModelBase {

  @FXML
  private JFXButton faulkner1, faulkner2, faulkner3, faulkner4, faulkner5, mainL1, mainL2, mainG, main1, main2, main3, faulknerBtn, mainCampusBtn;
  @FXML
  private TilePane faulknerBtns, mainCampusBtns;
  private String floor = "1";
  private String building = "Faulkner";
  private static final String faulkner = "Faulkner";
  private static final String main = "Main Campus";
  private static final String unselectedStyle = "-fx-background-color: lightgray; -fx-background-radius: 30;";
  private static final String selectedStyle = "-fx-background-color: lightseagreen; -fx-background-radius: 30;";

  /**
   * Sets the floor of the application
   */
  @FXML
  private void setFloor(ActionEvent event) {
    val target = (JFXButton) event.getSource();
    floor = target.getText();
    switch (building) {
      case (faulkner):
        for (JFXButton btn : Arrays
            .asList(faulkner1, faulkner2, faulkner3, faulkner4, faulkner5)) {
          btn.setStyle(unselectedStyle);
        }
        break;
      case (main):
        for (JFXButton btn : Arrays
            .asList(mainL2, mainL1, mainG, main1, main2, main3)) {
          btn.setStyle(unselectedStyle);
        }
        break;
    }
    target.setStyle(selectedStyle);
    dispatch(new FloorSwitchEvent(floor, building));
  }

  /**
   * Sets the building
   */
  public void setBuilding(ActionEvent event) {
    val target = (JFXButton) event.getSource();
    building = target.getText();
    switch (building) {
      case (faulkner):
        for (JFXButton btn : Arrays
            .asList(faulkner2, faulkner3, faulkner4, faulkner5, mainCampusBtn)) {
          btn.setStyle(unselectedStyle);
        }
        faulkner1.setStyle(selectedStyle);
        faulknerBtns.setVisible(true);
        mainCampusBtns.setVisible(false);
        break;
      case (main):
        for (JFXButton btn : Arrays
            .asList(main2, main3, mainG, mainL1, mainL2, faulknerBtn)) {
          btn.setStyle(unselectedStyle);
        }
        main1.setStyle(selectedStyle);
        mainCampusBtns.setVisible(true);
        faulknerBtns.setVisible(false);
        break;
    }
    target.setStyle(selectedStyle);
    floor = "1";
    dispatch(new FloorSwitchEvent(floor, building));
  }
}