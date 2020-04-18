package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.model.language.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminViewModel extends MainViewModelBase {

  @FXML
  @Language(key = "adminWelcome")
  private Label welcomeLabel;

  @FXML
  @Language(key = "adminLeftButton")
  private Button leftButton;

  @FXML
  @Language(key = "adminRightButton")
  private Button rightButton;

  // todo get labels from Main.fxml and annotate them with Language to specify the text to use

  @Override
  public void onLeftButton(ActionEvent event) {
    dispatch(new ForwardNavigationEvent("adminLeftButton",
        "views/admin/FloorMapEditor.fxml"));
  }

  @Override
  public void onRightButton(ActionEvent event) {
    dispatch(new ForwardNavigationEvent("adminRightButton",
        "views/admin/RequestHandler.fxml"));
  }
}
