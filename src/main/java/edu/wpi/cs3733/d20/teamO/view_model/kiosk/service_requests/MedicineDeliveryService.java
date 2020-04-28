package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.MedicineDeliveryServiceData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MedicineDeliveryService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  @FXML
  private VBox root;
  @FXML
  private JFXTextField patientName, medicationName;
  @FXML
  private JFXComboBox deliveryMethod, floorNumber, roomName;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    deliveryMethod.getItems().addAll("Oral", "Topical", "Injectable");
    super.setLocations(floorNumber, roomName);
  }

  @FXML
  public void submitRequest() {
    val valid = validator
        .validate(patientName, medicationName, deliveryMethod, floorNumber, roomName);
    if (valid) {
      val data = new MedicineDeliveryServiceData(medicationName.toString(),
          deliveryMethod.getSelectionModel().getSelectedItem().toString());
      Node requestNode = null;
      for (Node node : database.exportNodes().values()) {
        if (node.getLongName().equals(roomName.getSelectionModel().getSelectedItem().toString())) {
          requestNode = node;
          break;
        }
      }
      val time = LocalTime.now();
      val confirmationCode = database
          .addServiceRequest(time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
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
    super.close();
  }
}
