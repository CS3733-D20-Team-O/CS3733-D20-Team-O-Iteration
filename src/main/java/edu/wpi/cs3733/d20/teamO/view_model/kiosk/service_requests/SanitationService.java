package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SanitationService extends ViewModelBase {

  private final DatabaseWrapper database;
  //private final Validator validator;

  @FXML
  private JFXTextField requesterName;
  @FXML
  private JFXComboBox<String> floor, location;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @FXML
  private void submitRequest() {
    // to get time: https://stackoverflow.com/a/26225884/10003008
//    if (validator.validate(requesterName, floor, location)) {
//      val requestData = new SanitationRequestData(
//          ((JFXRadioButton) levelSelection.getSelectedToggle()).getText(),
//          additionalNotes.getText());
//      // val confirmationCode = database.addServiceRequest()
//      // todo show dialog with confirmation number, or snackbar error
//    }
  }

  @FXML
  private void cancelRequest() {
    // todo
  }
}
