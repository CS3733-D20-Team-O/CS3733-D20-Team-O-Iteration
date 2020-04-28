package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.MedicineDeliveryServiceData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MedicineDeliveryService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  @FXML
  private JFXTextField patientName, medicationName;
  @FXML
  private JFXComboBox deliveryMethod, floorNumber, roomName;
  @FXML
  JFXTimePicker timePicker;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    deliveryMethod.getItems().addAll("Oral", "Topical", "Injectable");
    setLocations(floorNumber, roomName);
  }

  @FXML
  public void submitRequest() {
    val valid = validator
        .validate(patientName, medicationName, deliveryMethod, floorNumber, roomName, timePicker);
    if (valid) {
      val data = new MedicineDeliveryServiceData(medicationName.getText(),
          deliveryMethod.getSelectionModel().getSelectedItem().toString());
      Node requestNode = null;
      val time = timePicker.getValue().format(DateTimeFormatter.ofPattern("HH:mm"));
      val confirmationCode = database
          .addServiceRequest(time,
              requestNode.getLongName(), "Medicine delivery", patientName.getText(), data);
      if (confirmationCode == null) {
        snackBar.show("Failed to create medicine delivery service request");
      } else {
        dialog.showBasic("Success", "Your confirmation code is:\n" + confirmationCode, "Close");
      }
    }
  }

  @FXML
  public void cancel() {
    close();
  }
}
