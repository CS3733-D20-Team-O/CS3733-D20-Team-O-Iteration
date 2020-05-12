package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InfoTechRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InfoTechService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXTextField requesterName;
  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private JFXComboBox<String> ITProblems;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    nodeSelector.setNodes(database.exportNodes().values());

    // Populate the combo box with IT service request problems
    ITProblems.setItems(FXCollections.observableArrayList(
        "Wireless Connection",
        "Kiosk",
        "Malware",
        "Other"));
  }

  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, nodeSelector, ITProblems, additionalNotes)) {
      val requestData = new InfoTechRequestData(
          ITProblems.getValue(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString(); // todo format this
      // todo use enum for sanitation string below
      // todo extract strings
      val confirmationCode = database.addServiceRequest(
          time, nodeSelector.getSelectedNode().getLongName(),
          "InfoTech", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the IT service request");
      } else {
        close();
        try {
          ((RequestConfirmationViewModel)
              dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
              .setServiceRequest(confirmationCode);
        } catch (IOException e) {
          log.error("Failed to show the detailed confirmation dialog", e);
          dialog.showBasic("IT Service Request Submitted Successfully",
              "Your confirmation code is:\n" + confirmationCode, "Close");
        }
      }
    }
  }

  @FXML
  private void closeRequest() {

  }
}
