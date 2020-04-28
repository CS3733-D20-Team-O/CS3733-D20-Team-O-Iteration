package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InternalTransportationService extends ServiceRequestBase {

  private enum State {
    ASSISTED,
    UNASSISTED
  }

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  private State currentState;

  @FXML
  private JFXTextField reqName;
  @FXML
  private JFXTimePicker reqTime;
  @FXML
  private ToggleGroup assistedToggle, unassistedToggle;
  @FXML
  private JFXComboBox<Integer> destinationFloor, currentFloor;
  @FXML
  private JFXComboBox<String> currentRoom, destinationRoom;


  @Override
  protected void start(URL location, ResourceBundle resources) {
    currentState = State.ASSISTED;
    setLocations(currentFloor, currentRoom);
    setLocations(destinationFloor, destinationRoom);

    //set radio buttons to default
    unassistedToggle.getToggles().get(0).setSelected(true);
    assistedToggle.getToggles().get(0).setSelected(true);

  }

  @FXML
  private void onSubmit() {
    switch (currentState) {
      case ASSISTED:
        if (validator.validate(currentRoom, reqName, reqTime, destinationRoom)) {
          val data = new InternalTransportationRequestData("Assisted",
              ((RadioButton) assistedToggle.getSelectedToggle()).getText(),
              destinationFloor.getSelectionModel().getSelectedItem().toString());
          addToDatabase(data);
        }
        break;
      default:
      case UNASSISTED:
        if (validator.validate(currentRoom, reqName, reqTime)) {
          val data = new InternalTransportationRequestData(
              "Unassisted",
              ((RadioButton) unassistedToggle.getSelectedToggle()).getText(), "None required");
          addToDatabase(data);
        }
        break;
    }
  }

  /**
   * adds a service request to the database
   *
   * @param data the ServiceRequestData accompanying the request
   */
  private void addToDatabase(ServiceRequestData data) {
    val confirmationCode = database
        .addServiceRequest(reqTime.toString(), currentRoom.getSelectionModel().getSelectedItem(),
            "Internal Transportation",
            reqName.getText(), data);

    if (confirmationCode == null) {
      snackBar.show("Failed to create the internal transportation request");
    } else {
      close();
      try {
        ((RequestConfirmationViewModel)
            dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
            .setServiceRequest(confirmationCode);
      } catch (IOException e) {
        log.error("Failed to show the detailed confirmation dialog", e);
        dialog.showBasic("Internal Transportation Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");
      }
    }
  }

  /**
   * function to change state of system to UNASSISTED when the view is in the unassisted service
   * tab
   */
  @FXML
  private void unassistedSelected() {
    currentState = State.UNASSISTED;
  }

  /**
   * function to change state of system to ASSISTED when the view is in the assisted service tab
   */
  @FXML
  private void assistedSelected() {
    currentState = State.ASSISTED;
  }

}
