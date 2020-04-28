package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.AVRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AVService extends ServiceRequestBase {

  @Inject
  private final DatabaseWrapper database;

  @Setter
  private Predicate<Node> locationFilter = n -> true;

  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private JFXButton submitButton, clearButton;
  @FXML
  private JFXComboBox<String> durationComboBox, serviceRequestComboBox, locationComboBox;
  @FXML
  private JFXComboBox<Integer> floorNumberComboBox;
  @FXML
  private JFXTextField requesterNameField;
  @FXML
  private JFXTextArea commentTextArea;
  @FXML
  private Label requesterNameLabel, floorNumberLabel, roomLabel, startTimeLabel, durationLabel,
      serviceRequestLabel, commentLabel;
  @FXML
  private JFXTimePicker startTimeTimePicker;

  ArrayList<Node> listOfRooms = new ArrayList<>();

  @Override
  protected void start(URL location, ResourceBundle resources) {
    setupComboBoxes();
    setLocations(floorNumberComboBox, locationComboBox);
  }

  // set up combo boxes
  public void setupComboBoxes() {
    System.out.println("in setupComboBoxes");

    // add times to duration CB
    String time = "";
    for (int i = 0; i < 60; i += 5) {
      time = time.concat(Integer.toString(i)).concat(" minutes");
      durationComboBox.getItems().add(time);
    }

    // add services to service request CB
    String[] services = {"Music", "Video Call"};
    for (String service : services) {
      durationComboBox.getItems().add(service);
    }

    setupComboFloorAndRoomComboBoxes();
  }

  public void setupComboFloorAndRoomComboBoxes() {
    // add floors to floor CB
    floorNumberComboBox.getItems().addAll(1, 2, 3, 4, 5);

    // add rooms to room CB
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();
      val roomNode = node.getValue();

      if (!listOfRooms.contains(roomNode) &&
          !node.getValue().getNodeType().equals("STAI") &&
          !node.getValue().getNodeType().equals("ELEV") &&
          !node.getValue().getNodeType().equals("REST") &&
          !node.getValue().getNodeType().equals("HALL")) {
        listOfRooms.add(roomNode);
        locationComboBox.getItems().add(roomToAdd);
      }
    }

    // Populate the floor CB with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floorNumberComboBox.getItems()::add);

    // Set up the populating of locations on each floor
    floorNumberComboBox.getSelectionModel().selectedItemProperty()
        .addListener((o, oldFloor, newFloor) -> {
          locationComboBox.getItems().clear();
          database.exportNodes().values().stream()
              .filter(node -> node.getFloor() == newFloor)
              .filter(locationFilter)
              .map(Node::getLongName).sorted()
              .forEachOrdered(locationComboBox.getItems()::add);
        });

    // Preselect the first floor and the first location on that floor
    if (!floorNumberComboBox.getItems().isEmpty()) {
      floorNumberComboBox.getSelectionModel().select(0);
      locationComboBox.getSelectionModel().select(0);
    }
  }

  @FXML
  private void submitRequest() {
    if (validator
        .validate(requesterNameField, floorNumberComboBox, locationComboBox, durationComboBox,
            serviceRequestComboBox)) {
      val time = LocalDateTime.now().toString();
      val requestData = new AVRequestData(
          serviceRequestComboBox.getSelectionModel().getSelectedItem(),
          commentTextArea.getText(),
          durationComboBox.getSelectionModel().getSelectedItem(),
          time);
      val confirmationCode = database.addServiceRequest(time,
          locationComboBox.getSelectionModel().getSelectedItem(),
          "A/V", requesterNameField.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the A/V service request");
      } else {
        dialog.showBasic("A/V Request Submitted Successfully",
            "Your confirmation code is:\n" + confirmationCode, "Close");
      }
    }
  }
}
