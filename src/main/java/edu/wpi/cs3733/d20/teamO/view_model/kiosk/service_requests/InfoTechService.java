package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InfoTechRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InfoTechService extends ViewModelBase {

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
  private JFXComboBox ITProblems;
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

    // Populate the combo box with IT service request problems
    ITProblems.setItems(FXCollections.observableArrayList(
        "Wireless Connection",
        "Kiosk",
        "Malware",
        "Other"));
  }

  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, floors, locations, additionalNotes)) {
      val requestData = new InfoTechRequestData(
          ITProblems.getValue().toString(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString(); // todo format this
      // todo use enum for sanitation string below
      // todo extract strings
      val confirmationCode = database.addServiceRequest(
          time, locations.getSelectionModel().getSelectedItem(),
          "InfoTech", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the IT service request");
      } else {
        closeRequest();
        dialog.showBasic("IT Service Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");

      }
    }
  }

  @FXML
  private void closeRequest() {

  }
}
