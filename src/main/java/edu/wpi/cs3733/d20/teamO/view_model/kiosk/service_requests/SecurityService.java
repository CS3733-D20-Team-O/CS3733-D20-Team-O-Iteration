package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SecurityService extends ServiceRequestBase {
  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXTextField requesterName;
  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());
  }

  //todo Submit and Cancel Requests
  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, nodeSelector)) {
      val requestData = new SecurityRequestData(
          ((JFXRadioButton) levelSelection.getSelectedToggle()).getText(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString();
      val confirmationCode = database.addServiceRequest(
          time, nodeSelector.getSelectedNode().getLongName(),
          "Security", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the Security service request");
      } else {
        close();
        try {
          ((RequestConfirmationViewModel)
              dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
              .setServiceRequest(confirmationCode);
        } catch (IOException e) {
          log.error("Failed to show the detailed confirmation dialog", e);
          dialog.showBasic("Emergency Security Request Submitted Successfully",
              "Your confirmation code is:\n" + confirmationCode, "Close");
        }
      }
    }
  }
}