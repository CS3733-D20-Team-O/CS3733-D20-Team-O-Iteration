package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.model.language.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KioskViewModel extends MainViewModelBase {

  @FXML
  @Language(key = "kioskWelcome")
  private Label welcomeLabel;

  @FXML
  @Language(key = "kioskLeftButton")
  private Button leftButton;

  @FXML
  @Language(key = "kioskRightButton")
  private Button rightButton;

  // todo get labels from Main.fxml and annotate them with Language to specify the text to use

  @Override
  public void onLeftButton(ActionEvent event) {
    dispatch(new ForwardNavigationEvent("kioskLeftButton",
        "views/kiosk/FindPath.fxml"));
//    new Thread(() -> {
//      try {
//        Thread.sleep(1000);
//        Platform.runLater(() -> dispatch(new BackwardNavigationEvent()));
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }).start();
  }

  @Override
  public void onRightButton(ActionEvent event) {
    dispatch(new ForwardNavigationEvent("kioskRightButton",
        "views/kiosk/ServiceRequestSelection.fxml"));
  }
}
