package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainKioskViewModel extends ViewModelBase {

  private final LanguageHandler languageHandler;

  @FXML
  private JFXComboBox<String> languageSwitcher;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the prompt text to the currently selected language
    val currentLocale = languageHandler.getCurrentLocale();
    languageSwitcher.setPromptText(currentLocale.getDisplayName(currentLocale));
    // Load in the supported locales
    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
      languageSwitcher.getItems().add(locale.getDisplayName(locale));
    }
    // Add a listener to switch to the selected locale
    languageSwitcher.getSelectionModel().selectedIndexProperty().addListener(
        ((observableValue, oldValue, newValue) -> languageHandler
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
