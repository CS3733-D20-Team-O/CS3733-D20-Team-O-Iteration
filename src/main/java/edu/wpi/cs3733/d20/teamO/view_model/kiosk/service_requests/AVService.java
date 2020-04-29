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
import java.time.format.DateTimeFormatter;
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
    setLocations(floorNumberComboBox, locationComboBox);
  }

  @FXML
  private void onSubmitPress() {
    if (!validator
        .validate(requesterNameField, floorNumberComboBox, locationComboBox, durationComboBox,
            serviceRequestComboBox)) {
      dialog.showBasic("Missing Information",
          "Please fill out the form completely to make request.",
          "OK");
      return;
    }

    if (!checkName()) {
      requesterNameField.clear();
      validator.validate(requesterNameField);
      return;
    }

    if (!checkDuration()) {
      durationComboBox.getSelectionModel().clearSelection();
      validator.validate(durationComboBox);
      return;
    }

    if (!checkRequest()) {
      serviceRequestComboBox.getSelectionModel().clearSelection();
      validator.validate(serviceRequestComboBox);
      return;
    }

    if (!checkFloor()) {
      floorNumberComboBox.getSelectionModel().clearSelection();
      validator.validate(floorNumberComboBox);
      return;
    }

    if (!checkLocation()) {
      locationComboBox.getSelectionModel().clearSelection();
      validator.validate(locationComboBox);
      return;
    }

    generateRequest();
    close();
  }

  private void generateRequest() {
    val time = startTimePicker.getValue().format(DateTimeFormatter.ofPattern("HH:mm:"));
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

  private boolean checkName() {
    return requesterNameField.getText().isEmpty();
  }

  private boolean checkDuration() {
    return durationComboBox.getSelectionModel().getSelectedItem().isEmpty();
  }

  private boolean checkRequest() {
    return serviceRequestComboBox.getSelectionModel().getSelectedItem().isEmpty();
  }

  private boolean checkFloor() {
    return (floorNumberComboBox.getSelectionModel().getSelectedItem() > 0);
  }

  private boolean checkLocation() {
    return locationComboBox.getSelectionModel().getSelectedItem().isEmpty();
  }
}
