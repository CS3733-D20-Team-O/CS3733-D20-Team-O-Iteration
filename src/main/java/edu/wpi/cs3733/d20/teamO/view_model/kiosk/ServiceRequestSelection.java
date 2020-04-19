package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ServiceRequestSelection extends ViewModelBase {

  @FXML
  private AnchorPane root;
  @FXML
  private TextField confirmationCode;
  @FXML
  private JFXComboBox<String> serviceSelector;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    val descriptionToFXML = new HashMap<String, String>();
    descriptionToFXML.put(getString("serviceGiftDeliveryDescription"),
        "views/kiosk/service_requests/GiftDeliveryService.fxml");
    descriptionToFXML.keySet().forEach(title -> serviceSelector.getItems().add(title));
    serviceSelector.getSelectionModel().selectedItemProperty()
        .addListener(((observable, oldValue, newValue) ->
            dispatch(new ForwardNavigationEvent(newValue, descriptionToFXML.get(newValue)))));
  }

  @FXML
  private void onSubmitClicked() {
//    for (val request : get(DatabaseWrapper.class).exportServiceRequests()) {
//      if (request.getRequestID().equals(confirmationCode.getText())) {
//
//        // todo open the request info in a JFXDialog
//        return;
//      }
//    }
    val stackPane = new StackPane();
    AnchorPane.setTopAnchor(stackPane, 0.0);
    AnchorPane.setBottomAnchor(stackPane, 0.0);
    AnchorPane.setLeftAnchor(stackPane, 0.0);
    AnchorPane.setRightAnchor(stackPane, 0.0);
    JFXDialog dialog = new JFXDialog();
    dialog.setContent(new Label("Content"));
    dialog.show(stackPane);
    root.getChildren().add(stackPane);
    //dialog.overlayCloseProperty().addListener(((observable, oldValue, newValue) -> root.getChildren().remove(stackPane)));
    JFXSnackbar bar = new JFXSnackbar(root);
    bar.enqueue(
        new SnackbarEvent(new Label("No service request with that confirmation number was found")));
  }
}
