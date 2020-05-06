package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ExternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ExternalTransportationService extends ServiceRequestBase {

  //needs: requester name, time, floor, location, transportation type, destination, additional notes

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXTextField requesterName, destination;
  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private JFXComboBox<String> transportationType;
  @FXML
  private JFXTimePicker timePicker;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());

    transportationType.getItems().add("Emergency Air Ambulance");
    transportationType.getItems().add("Non-Emergency Ground Ambulance");
    transportationType.getItems().add("Medical Car and Wheelchair Van");
    transportationType.getItems().add("Local Ground Ambulance Emergency Medical Services");
    transportationType.getItems().add("Shuttle Service");
  }

  @FXML
  private void submitRequest() {
    if (validator
        .validate(requesterName, nodeSelector, transportationType, timePicker, destination)) {
      val requestData = new ExternalTransportationRequestData(
          transportationType.getSelectionModel().getSelectedItem(),
          destination.getText(),
          additionalNotes.getText());
      val time = timePicker.getValue().toString();
      // todo extract strings
      val confirmationCode = database.addServiceRequest(
          time, nodeSelector.getSelectedNode().getLongName(),
          "External Transportation", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the external transportation service request");
      } else {
        cancelRequest();
        dialog.showBasic("External Transportation Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");

      }
    }
  }

  @FXML
  public void cancelRequest() {
    //close();
  }
}
