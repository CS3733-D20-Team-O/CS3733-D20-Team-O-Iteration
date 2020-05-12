package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.events.CSSSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainKioskViewModel extends ViewModelBase {

  private final LanguageHandler languageHandler;
  private final DatabaseWrapper database;
  private final LoginDetails loginDetails;
  private final Validator validator;
  private final Navigator navigator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private StackPane welcomeBar, container;
  @FXML
  private HBox langContainer;
  @FXML
  private VBox contentContainer;
  @FXML
  private JFXTextField confirmationCode;
  @FXML
  private JFXComboBox<String> serviceSelector, languageSwitcher;
  @FXML
  @Getter
  private JFXButton lookupButton; // Used for clicks in testing

  @Override
  protected void start(URL location, ResourceBundle resources) {

    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(welcomeBar, 2);
    JFXDepthManager.setDepth(contentContainer, 3);
    JFXDepthManager.setDepth(langContainer, 1);

    // Set the prompt text to the currently selected language
    val currentLocale = languageHandler.getCurrentLocale();
    languageSwitcher.setPromptText(currentLocale.getDisplayName(currentLocale));
    // Load in the supported locales
    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
      languageSwitcher.getItems().add(locale.getDisplayName(locale));
    }
    // Add a listener to switch to the selected locale
    languageSwitcher.getSelectionModel().selectedIndexProperty().addListener(((o, old, index) ->
        languageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[index.intValue()])));

    // Load the admin dialog if appropriate
    if (loginDetails.isValid()) {
      // Put this in a run later so it doesn't mess up navigation
      Platform.runLater(this::openAdminDialog);
    }
  }

  @FXML
  private void openLoginDialog() {

    val header = new Label("Login");
    header.setStyle("-fx-font-size: 24");
    val username = new JFXTextField();
    username.setPromptText("Username");
    username.setValidators(new RequiredFieldValidator("Username is required"));
    val password = new JFXPasswordField();
    password.setPromptText("Password");
    password.setValidators(new RequiredFieldValidator("Password is required"));
    val loginButton = new JFXButton("LOGIN");
    val closeButton = new JFXButton("CLOSE");
    val buttonContainer = new HBox(loginButton, closeButton);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
    val root = new VBox(header, username, password, buttonContainer);
    root.setAlignment(Pos.CENTER);
    root.setSpacing(32);
    root.setPrefWidth(300);
    root.setStyle("-fx-font-size: 16; -fx-padding: 32");
    val jfxDialog = dialog.showFullscreen(root);
    closeButton.setOnAction(e -> jfxDialog.close());
    loginButton.setOnAction(e -> {
      if (validator.validate(username, password)) {
        loginDetails.setUsername(username.getText());
        loginDetails.setPassword(password.getText());
        if (loginDetails.isValid()) {
          jfxDialog.close();
          openAdminDialog();
        } else {
          loginDetails.reset();
          snackBar.show("Login failed; invalid credentials?");
        }
      }
    });
  }

  @FXML
  private void openAbout() {
    try {
      dialog.showFullscreenFXML("views/kiosk/About.fxml");
    } catch (IOException e) {
      log.error("Could not load the about page", e);
    }
  }

  @FXML
  private void openAdminDialog() {
    try {
      dialog.showFXML(container, "views/admin/Main.fxml");
    } catch (IOException e) {
      log.error("Could not load the admin dialog", e);
    }
  }

  @FXML
  private void openHowToUseKiosk() {
    try {
      dialog.showFXML(container, "views/kiosk/HowToUseKiosk.fxml");
    } catch (IOException e) {
      log.error("Could not load the dialog", e);
    }
  }

  @FXML
  private void openPathFinder() {
    try {
      navigator.push(getString("mainLeftButton"), "views/kiosk/FindPath.fxml");
    } catch (IOException e) {
      log.error("Failed to open the path finder", e);
    }
  }

  @FXML
  public void openServiceRequestSelect() {
    try {
      dialog.showFXML(container, "views/kiosk/ServiceSelector.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request Screen", e);
    }
  }

  @FXML
  private void lookupServiceRequest() {
    for (val request : database.exportServiceRequests()) {
      if (request.getRequestID().equals(confirmationCode.getText().toUpperCase())) {
        try {
          ((RequestConfirmationViewModel)
              dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
              .setServiceRequest(request.getRequestID());
          return;
        } catch (IOException e) {
          log.error("Failed to show the service request confirmation dialog", e);
        }
      }
    }
    // If we didn't return in the for loop, that means the request isn't in the database
    snackBar.show(getString("serviceRequestLookupFail"));
  }

  public void onEvent(Event event) {
    if (event.getClass().equals(CSSSwitchEvent.class)) {
      String path = ((CSSSwitchEvent) event).getPath();
      System.out.println("Clicked");
      // todo set the CSS to path
    }
  }
}
