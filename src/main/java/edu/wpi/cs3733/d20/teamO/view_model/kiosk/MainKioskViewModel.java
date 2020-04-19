package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

public class MainKioskViewModel extends ViewModelBase {

  @FXML
  private JFXComboBox<String> languageSwitcher;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // todo set languageSwitcher langs
    // todo listen to language switcher langs
  }

  @FXML
  private void openPathFinder() {
    dispatch(new ForwardNavigationEvent(getString("mainLeftButton"),
        "views/kiosk/FindPath.fxml"));
  }

  @FXML
  private void openRequestScreen() {
    dispatch(new ForwardNavigationEvent(getString("mainRightButton"),
        "views/kiosk/ServiceRequestSelection.fxml"));
  }
}
