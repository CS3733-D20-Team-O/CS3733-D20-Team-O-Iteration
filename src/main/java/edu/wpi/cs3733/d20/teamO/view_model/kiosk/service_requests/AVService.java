package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.AVRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AVService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Dialog dialog;
  private final Validator validator;
  private final SnackBar snackBar;

  @FXML
  private JFXComboBox<String> durationComboBox, serviceRequestComboBox, locationComboBox;
  @FXML
  private JFXComboBox<Integer> floorNumberComboBox;
  @FXML
  private JFXTextField requesterNameField;
  @FXML
  private JFXTextArea commentTextArea;
  @FXML
  private JFXTimePicker startTimePicker;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    setupComboBoxes();
    setLocations(floorNumberComboBox, locationComboBox);
  }

  // set up combo boxes
  public void setupComboBoxes() {
    // add times to duration CB
    for (int i = 0; i <= 60; i += 5) {
      val time = "";
      time = i + " minutes";
      durationComboBox.getItems().add(time);
    }
  }

  @FXML
  private void submitRequest() {
    if (validator
        .validate(requesterNameField, floorNumberComboBox, locationComboBox, durationComboBox,
            serviceRequestComboBox)) {
      startTimePicker = new JFXTimePicker();
      val time = startTimePicker.getValue().toString();
      val requestData = new AVRequestData(
          serviceRequestComboBox.getSelectionModel().getSelectedItem(),
          time,
          durationComboBox.getSelectionModel().getSelectedItem(),
          commentTextArea.getText());
      val confirmationCode = database.addServiceRequest(time,
          locationComboBox.getSelectionModel().getSelectedItem(),
          "A/V", requesterNameField.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the A/V service request");
      } else {
        close();
        try {
          ((RequestConfirmationViewModel)
              dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
              .setServiceRequest(confirmationCode);
        } catch (IOException e) {
          log.error("Failed to show the detailed confirmation dialog", e);
          dialog.showBasic("A/V Request Submitted Successfully",
              "Your confirmation code is:\n" + confirmationCode, "CLOSE");
        }
      }
    }
  }
}
