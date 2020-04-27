package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
  private VBox centerBox;
  @FXML
  private JFXTextField patientName, medicationName;
  @FXML
  private JFXComboBox deliveryMethod, floorNumber, roomName;

  @Override
  protected void start(URL location, ResourceBundle resources) {
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

  public void submitRequest(ActionEvent actionEvent) {
    val valid = validator
        .validate(patientName, medicationName, deliveryMethod, floorNumber, roomName);
    if (valid) {

    }
  }

  public void cancel(ActionEvent actionEvent) {
  }
}
