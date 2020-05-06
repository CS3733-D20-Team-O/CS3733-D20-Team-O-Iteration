package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.MedicineDeliveryServiceData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MedicineDeliveryService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  @FXML
  private JFXTextField patientName, medicationName;
  @FXML
  private JFXComboBox deliveryMethod;
  @FXML
  private JFXTimePicker timePicker;
  @FXML
  private NodeSelector nodeSelector;

  private final SnackBar snackBar;
  private final Dialog dialog;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    deliveryMethod.getItems().addAll("Oral", "Topical", "Injectable");
    nodeSelector.setNodes(database.exportNodes().values());
  }

  @FXML
  public void submitRequest(ActionEvent event) {
    val valid = validator
        .validate(patientName, medicationName, deliveryMethod, nodeSelector, timePicker);
    if (valid) {
      val data = new MedicineDeliveryServiceData(medicationName.getText(),
          deliveryMethod.getSelectionModel().getSelectedItem().toString());
      val time = timePicker.getValue().toString();
      val confirmationCode = database
          .addServiceRequest(time,
              nodeSelector.getSelectedNode().getLongName(), "Medicine delivery",
              patientName.getText(), data);
      if (confirmationCode == null) {
        snackBar.show("Failed to create medicine delivery service request");
      } else {
        close();
        showRequestConfirmation(confirmationCode);
      }
    }
  }

  @FXML
  public void cancel() {
    close();
  }
}
