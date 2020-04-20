package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
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
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ServiceRequestSelection extends ViewModelBase {

  @FXML
  private AnchorPane root;
  @FXML
  private JFXTextField confirmationCode;
  @FXML
  private JFXComboBox<String> serviceSelector;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Create a map of available descriptions to their corresponding fxml files
    val descriptionToFXML = new HashMap<String, String>();
    descriptionToFXML.put(getString("serviceGiftDeliveryDescription"),
        "views/kiosk/service_requests/GiftDeliveryService.fxml");
    // Add each description to the combobox
    descriptionToFXML.keySet().forEach(title -> serviceSelector.getItems().add(title));
    // Whenever a service is requested in the combobox, navigate to it
    serviceSelector.getSelectionModel().selectedItemProperty()
        .addListener(((observable, oldValue, newValue) ->
            dispatch(new ForwardNavigationEvent(newValue, descriptionToFXML.get(newValue)))));
  }

  @FXML
  private void onSubmitClicked() {
    for (val request : get(DatabaseWrapper.class).exportServiceRequests()) {
      if (request.getRequestID().equals(confirmationCode.getText())) {
        showRequestConfirmationDialog(request);
        return;
      }
    }
    // If we didn't return in the for loop, that means the request isn't in the database
    showRequestNotFoundSnackbar();
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
    val node = get(DatabaseWrapper.class).exportNodes().get(request.getRequestNode());
    val infoText = getString("serviceRequestConfirmationID") + ": " + request.getRequestID()
        + "\n" + getString("serviceRequestAssignedTo") + ": " + request.getEmployeeAssigned()
        + "\n" + getString("serviceRequestLocation") + ": " + node.getLongName()
        + "\n" + getString("serviceRequestFloor") + ": " + node.getFloor();
    val closeButton = new JFXButton(getString("serviceRequestDialogClose"));
    closeButton.setOnAction(e -> dialog.close());
    val deleteButton = new JFXButton(getString("serviceRequestDialogCancelRequest"));
    deleteButton.setOnAction(e -> {
      get(DatabaseWrapper.class).deleteFromTable(Table.SERVICE_REQUESTS_TABLE,
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
}
