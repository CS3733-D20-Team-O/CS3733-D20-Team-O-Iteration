package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import com.google.inject.Inject;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import java.net.URL;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SecurityService extends ViewModelBase {
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
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floors.getItems()::add);

    // Set up the populating of locations on each floor
    floors.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) ->
        database.exportNodes().values().stream()
            .filter(node -> newFloor.equals(node.getFloor()))
            .map(Node::getLongName).sorted()
            .forEachOrdered(locations.getItems()::add));

    // Preselect the first floor and the first location on that floor
    if (!floors.getItems().isEmpty()) {
      floors.getSelectionModel().select(0);
      locations.getSelectionModel().select(0);
    }
  }

//todo Submit and Cancel Requests
  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, floors, locations)) {
      val requestData = new SecurityRequestData(
          ((JFXRadioButton) levelSelection.getSelectedToggle()).getText(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString();
      val confirmationCode = database.addServiceRequest(
          time, locations.getSelectionModel().getSelectedItem(),
          "Security", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the Emergency Security service request");
      } else {
        cancelRequest();
        dialog.showBasic("Emergency Security Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");
      }
    }
  }

  @FXML
  private void cancelRequest() {

  }
}