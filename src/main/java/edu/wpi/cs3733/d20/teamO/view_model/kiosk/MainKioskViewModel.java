package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.val;

public class MainKioskViewModel extends ViewModelBase {

  @FXML
  private JFXComboBox<String> languageSwitcher;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the prompt text to the currently selected language
    val currentLocale = get(LanguageHandler.class).getCurrentLocale();
    languageSwitcher.setPromptText(currentLocale.getDisplayName(currentLocale));
    // Load in the supported locales
    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
      languageSwitcher.getItems().add(locale.getDisplayName(locale));
    }
    // Add a listener to switch to the selected locale
    languageSwitcher.getSelectionModel().selectedIndexProperty().addListener(
        ((observableValue, oldValue, newValue) -> get(LanguageHandler.class)
            .setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[newValue.intValue()])));
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
