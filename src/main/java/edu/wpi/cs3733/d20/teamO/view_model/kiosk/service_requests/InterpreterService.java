package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InterpreterData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InterpreterService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXTextField requesterName;
  @FXML
  private JFXComboBox<Integer> floors;
  @FXML
  private JFXComboBox<String> locations;
  @FXML
  private JFXComboBox<String> language;
  @FXML
  private JFXComboBox<String> gender;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    setLocations(this.floors, locations);

    language.getItems().add("Spanish");
    language.getItems().add("French");
    language.getItems().add("Chinese");
    language.getItems().add("Japanese");
    language.getItems().add("Russian");

    gender.getItems().add("Male");
    gender.getItems().add("Female");
    gender.getItems().add("No Preference");

  }

  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, floors, locations)) {
      val requestData = new InterpreterData(language.getValue(), gender.getValue(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString(); // todo format this
      // todo use enum for sanitation string below
      // todo extract strings
      val confirmationCode = database.addServiceRequest(
          time, locations.getSelectionModel().getSelectedItem(),
          "Interpreter", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the interpreter service request");
      } else {
        close();
        try {
          ((RequestConfirmationViewModel)
              dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml"))
              .setServiceRequest(confirmationCode);
        } catch (IOException e) {
          log.error("Failed to show the detailed confirmation dialog", e);
          dialog.showBasic("Interpreter Request Submitted Successfully",
              "Your confirmation code is:\n" + confirmationCode, "Close");
        }
      }
    }
  }
}
