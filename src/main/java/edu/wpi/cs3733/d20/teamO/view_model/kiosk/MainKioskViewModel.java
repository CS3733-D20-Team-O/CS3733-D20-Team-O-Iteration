package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
  private final Navigator navigator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private AnchorPane root;
  @FXML
  private HBox welcomeBar, langContainer;
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
    languageSwitcher.getSelectionModel().selectedIndexProperty().addListener(
        ((observableValue, oldValue, newValue) -> languageHandler
            .setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[newValue.intValue()])));

    // Create a map of available descriptions to their corresponding fxml files todo use registry
    val descriptionToFXML = new HashMap<String, String>();
    descriptionToFXML.put(getString("serviceGiftDeliveryDescription"),
        "views/kiosk/service_requests/GiftDeliveryService.fxml");
    // Add each description to the combobox
    descriptionToFXML.keySet().forEach(title -> serviceSelector.getItems().add(title));
    // Whenever a service is requested in the combobox, navigate to it and clear the selection
    serviceSelector.getSelectionModel().selectedItemProperty()
        .addListener(((observable, oldValue, newValue) -> {
          try {
            navigator.push(newValue, descriptionToFXML.get(newValue));
          } catch (IOException e) {
            log.error("Failed to open " + newValue, e);
          }
          serviceSelector.getSelectionModel().clearSelection();
        }));

    if (loginDetails.isValid()) {
      // todo load admin stuff into dialog
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
  private void lookupServiceRequest() {
    for (val request : database.exportServiceRequests()) {
      if (request.getRequestID().equals(confirmationCode.getText())) {
        showRequestConfirmationDialog(request);
        return;
      }
    }
    // If we didn't return in the for loop, that means the request isn't in the database
    snackBar.show(getString("serviceRequestLookupFail"));
  }

  /**
   * Creates and shows a dialog based on the provided request
   *
   * @param request the request to fill in the dialog information
   */
  private void showRequestConfirmationDialog(ServiceRequest request) {
    val header = new Label(request.getType() + " Service Request");
    val node = database.exportNodes().get(request.getRequestNode());
    val infoText = getString("serviceRequestConfirmationID") + ": " + request.getRequestID()
        + "\n" + getString("serviceRequestAssignedTo") + ": " + request.getEmployeeAssigned()
        + "\n" + getString("serviceRequestLocation") + ": " + node.getLongName()
        + "\n" + getString("serviceRequestFloor") + ": " + node.getFloor();
    val closeButton = new JFXButton(getString("serviceRequestDialogClose"));
    val deleteButton = new JFXButton(getString("serviceRequestDialogCancelRequest"));
    val buttonContainer = new HBox(deleteButton, closeButton);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
    val container = new VBox(header, new Label(infoText), buttonContainer);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(16));
    container.setSpacing(16);
    val requestConfirmationDialog = dialog.showFullscreen(container);
    closeButton.setOnAction(e -> requestConfirmationDialog.close());
    deleteButton.setOnAction(e -> {
      database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE,
          ServiceRequestProperty.REQUEST_ID, request.getRequestID());
      requestConfirmationDialog.close();
    });
  }
}
