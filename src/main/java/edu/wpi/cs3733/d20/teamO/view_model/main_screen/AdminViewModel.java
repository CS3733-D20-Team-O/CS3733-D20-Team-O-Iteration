package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.model.language.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminViewModel extends MainViewModelBase {

  // get labels from Main.fxml and annotate them with Language to specify the text to use
  // implement methods below
  @FXML
  @Language(key = "mainWelcome") // todo change to admin welcome
  private Label welcomeLabel;

  @Override
  public void onLeftButton(ActionEvent event) {
    // todo the floor map editor (new window for now)
  }

  @Override
  public void onRightButton(ActionEvent event) {
    // todo the service request list from database where you can edit (new window for now)
  }
}
