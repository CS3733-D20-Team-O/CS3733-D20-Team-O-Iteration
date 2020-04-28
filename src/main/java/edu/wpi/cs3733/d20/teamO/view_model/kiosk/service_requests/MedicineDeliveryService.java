package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.MedicineDeliveryServiceData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
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
public class MedicineDeliveryService extends ViewModelBase {

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
    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floorNumber.getItems()::add);
    // Set up the populating of locations on each floor
    floorNumber.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      roomName.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(roomName.getItems()::add);
      roomName.getSelectionModel().select(0);
    });
    // Preselect the first floor and the first location on that floor
    if (!floorNumber.getItems().isEmpty()) {
      floorNumber.getSelectionModel().select(0);
      roomName.getSelectionModel().select(0);
    }
  }

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
              requestNode.getNodeID(), "Medication delivery", patientName.getText(), data);
      if (confirmationCode == null) {
        snackBar.show("Failed to create medication delivery service request");
      } else {
        dialog.showBasic("Success", "Your confirmation code is:\n" + confirmationCode, "Close");
      }
    }
  }

  public void cancel() {
    // todo implement
  }
}
