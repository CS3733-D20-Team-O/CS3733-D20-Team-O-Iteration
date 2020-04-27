package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ExternalTransportationService extends ViewModelBase {

  private final DatabaseWrapper database;

  @FXML
  private JFXTextField requesterName, destination;
  @FXML
  private JFXComboBox<String> floor, location, transportationType;
  @FXML
  private JFXTextArea additionalNotes;

  /**
   * @return true if all fields of the view are valid (and can be processed), false otherwise
   */
  private boolean allFieldsValid() {
    return Stream.of(requesterName, floor, location, transportationType, destination)
        .allMatch(IFXValidatableControl::validate);
  }

  @FXML
  private void submitRequest() {
    if (allFieldsValid()) {
      val requestData = new SanitationRequestData(
          destination.getText(),
          additionalNotes.getText());
      // val confirmationCode = database.addServiceRequest()
      // todo show dialog with confirmation number, or snackbar error
    }
  }

  @FXML
  private void cancelRequest() {
    // todo
  }
}
