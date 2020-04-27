package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ExternalTransportationService extends ViewModelBase {

  //needs: requester name, destination, floor, location, transportation type, additional notes

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXTextField requesterName, destination;
  @FXML
  private JFXComboBox<Integer> floors;
  @FXML
  private JFXComboBox<String> locations;
  @FXML
  private JFXComboBox<String> transportationType;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floors.getItems()::add);

    // Set up the populating of locations on each floor
    floors.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      locations.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(locations.getItems()::add);
      locations.getSelectionModel().select(0);
    });

    // Preselect the first floor and the first location on that floor
    if (!floors.getItems().isEmpty()) {
      floors.getSelectionModel().select(0);
      locations.getSelectionModel().select(0);
    }
  }

  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, floors, locations)) {
      val requestData = new SanitationRequestData(
          transportationType.getSelectionModel().getSelectedItem(),
          additionalNotes.getText());
      val time = LocalDateTime.now().toString(); // todo format this
      // todo use enum for type of transportation below
      // todo extract strings
      val confirmationCode = database.addServiceRequest(
          time, locations.getSelectionModel().getSelectedItem(),
          "External Transportation", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the external transportation service request");
      } else {
        cancelRequest();
        dialog.showBasic("External Transportation Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");

      }
    }
  }

  @FXML
  private void cancelRequest() {
    // todo (dialog -> close manual, window, navigator.pop())
  }
}
