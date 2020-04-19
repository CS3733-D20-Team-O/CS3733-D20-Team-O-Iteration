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
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
        // Create a full-screen stack pane and add it to the window
        val stackPane = new StackPane();
        AnchorPane.setTopAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        root.getChildren().add(stackPane);
        // Create and show the dialog
        JFXDialog dialog = new JFXDialog();
        dialog.setPadding(new Insets(10));
        val infoText = getString("serviceRequestConfirmationNumber") + ": "
            + request.getRequestID()
            + "\n" + getString("serviceRequestAssignedTo") + ": "
            + request.getEmployeeAssigned();
        val closeButton = new JFXButton(getString("serviceRequestDialogClose"));
        closeButton.setOnAction(e -> dialog.close());
        val container = new VBox(new Label(infoText), closeButton);
        dialog.setContent(container);
        dialog.show(stackPane);
        dialog.addEventHandler(JFXDialogEvent.CLOSED, e -> root.getChildren().remove(stackPane));
        return;
      }
    }
    // If we didn't return in the for loop, that means the request isn't in the database
    JFXSnackbar bar = new JFXSnackbar(root);
    // todo style this snack bar
    bar.enqueue(new SnackbarEvent(new Label(getString("serviceRequestLookupFail"))));
  }
}
