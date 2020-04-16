package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.model.language.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KioskViewModel extends MainViewModelBase {

  @FXML
  @Language(key = "mainWelcome")
  private Label welcomeLabel;

  // get labels from Main.fxml and annotate them with Language to specify the text to use
  // implement methods below

  @Override
  public void onLeftButton(ActionEvent event) {
    // todo open path finding in new window (for now)
  }

  @Override
  public void onRightButton(ActionEvent event) {
    // todo open ServiceRequestSelection in new window (for now)
  }
}
