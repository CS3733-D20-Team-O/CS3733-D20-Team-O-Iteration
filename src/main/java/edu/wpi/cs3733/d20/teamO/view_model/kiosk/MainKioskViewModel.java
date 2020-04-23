package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainKioskViewModel extends ViewModelBase {

  private final LanguageHandler languageHandler;
  private final DatabaseWrapper database;

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
          dispatch(new ForwardNavigationEvent(newValue, descriptionToFXML.get(newValue)));
          serviceSelector.getSelectionModel().clearSelection();
        }));
  }

  @FXML
  private void openPathFinder() {
    dispatch(new ForwardNavigationEvent(getString("mainLeftButton"),
        "views/kiosk/FindPath.fxml"));
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
    showRequestNotFoundSnackbar();
  }

  // todo extract the below into other classes

  /**
   * Shows a snack bar that states the request was not found
   */
  private void showRequestNotFoundSnackbar() {
    JFXSnackbar bar = new JFXSnackbar(root);
    val label = new Label(getString("serviceRequestLookupFail"));
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    // Add 16 margin and 16 padding as per material design guidelines
    container.setStyle("-fx-background-color: #323232;  -fx-background-insets: 16");
    container.setPadding(new Insets(32)); // total padding, including margin
    bar.enqueue(new SnackbarEvent(container));
  }

  /**
   * Creates and shows a dialog based on the provided request
   *
   * @param request the request to fill in the dialog information
   */
  private void showRequestConfirmationDialog(ServiceRequest request) {
    // Create a full-screen stack pane and add it to the window
    val stackPane = new StackPane();
    AnchorPane.setTopAnchor(stackPane, 0.0);
    AnchorPane.setBottomAnchor(stackPane, 0.0);
    AnchorPane.setLeftAnchor(stackPane, 0.0);
    AnchorPane.setRightAnchor(stackPane, 0.0);
    root.getChildren().add(stackPane);

    // Create the dialog and its contents
    val dialog = new JFXDialog();
    val header = new Label(request.getType() + " Service Request");
    val node = database.exportNodes().get(request.getRequestNode());
    val infoText = getString("serviceRequestConfirmationID") + ": " + request.getRequestID()
        + "\n" + getString("serviceRequestAssignedTo") + ": " + request.getEmployeeAssigned()
        + "\n" + getString("serviceRequestLocation") + ": " + node.getLongName()
        + "\n" + getString("serviceRequestFloor") + ": " + node.getFloor();
    val closeButton = new JFXButton(getString("serviceRequestDialogClose"));
    closeButton.setOnAction(e -> dialog.close());
    val deleteButton = new JFXButton(getString("serviceRequestDialogCancelRequest"));
    deleteButton.setOnAction(e -> {
      database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE,
          ServiceRequestProperty.REQUEST_ID, request.getRequestID());
      dialog.close();
    });
    val buttonContainer = new HBox(deleteButton, closeButton);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
    val container = new VBox(header, new Label(infoText), buttonContainer);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(16));
    container.setSpacing(16);

    // Set dialog contents, close handler, and show
    dialog.setContent(container);
    dialog.addEventHandler(JFXDialogEvent.CLOSED, e -> root.getChildren().remove(stackPane));
    dialog.show(stackPane);
  }
}
