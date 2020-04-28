package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InternalTransportationService extends ServiceRequestBase {

  enum State {
    ASSISTED,
    UNASSISTED
  }

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  private State currentState;


  @FXML
  private JFXTextField reqName;
  @FXML
  private JFXTextField reqTime;

  @FXML
  private ToggleGroup assistedToggle, unassistedToggle;

  @FXML
  private JFXComboBox<Integer> destinationFloor, currentFloor;

  @FXML
  private JFXComboBox<String> currentRoom, destinationRoom;

  private Node entryNode;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    currentState = State.ASSISTED;
    setLocations(currentFloor, currentRoom);
    setLocations(destinationFloor, destinationRoom);

    //set radio buttons to default
    unassistedToggle.getToggles().get(0).setSelected(true);
    assistedToggle.getToggles().get(0).setSelected(true);

    // Preselect the first floor and the first location on that floor
    if (!currentFloor.getItems().isEmpty()) {
      currentFloor.getSelectionModel().select(0);
      currentRoom.getSelectionModel().select(0);
    }

    // Preselect the first floor and the first location on that floor
    if (!destinationFloor.getItems().isEmpty()) {
      destinationFloor.getSelectionModel().select(0);
      destinationRoom.getSelectionModel().select(0);
    }

  }

  @FXML
  private void onSubmit() {
    InternalTransportationRequestData data = null;

    switch (currentState) {
      case ASSISTED:
        if (validator.validate(currentRoom, reqName, reqTime, destinationRoom)) {
          data = new InternalTransportationRequestData("Assisted",
              ((RadioButton) assistedToggle.getSelectedToggle()).getText(),
              destinationFloor.getSelectionModel().getSelectedItem().toString());
        }
        break;
      case UNASSISTED:
        if (validator.validate(currentRoom, reqName, reqTime)) {
          data = new InternalTransportationRequestData("Unassisted",
              ((RadioButton) unassistedToggle.getSelectedToggle()).getText(), "None required");
        }
        break;
      default:
        break;
    }

    database.addServiceRequest(reqTime.getText(), entryNode.getNodeID(), "Int. Transport",
        reqName.getText(), data);
  }

  /**
   * function to change state of system to UNASSISTED when the view is in the unassisted service
   * tab
   */
  @FXML
  private void unassistedSelected() {
    currentState = State.UNASSISTED;
  }

  /**
   * function to change state of system to ASSISTED when the view is in the assisted service tab
   */
  @FXML
  private void assistedSelected() {
    currentState = State.ASSISTED;
  }

  /**
   * function to display appropriate rooms when a floor is selected in current floor
   */
  @FXML
  private void doCurrentFloor() {
    doFloor(currentRoom, currentFloor.getSelectionModel().getSelectedItem().toString());
  }

  /**
   * function to display appropriate rooms when a floor is selected in destination floor
   */
  @FXML
  private void doDestinationFloor() {
    doFloor(destinationRoom, destinationFloor.getSelectionModel().getSelectedItem().toString());
  }

  /**
   * takes a combobox of floors and populates the corresponding room combo box with appropriate
   * values
   *
   * @param rooms the combobox to populate
   * @param floor           the floor selected
   */
  private void doFloor(JFXComboBox rooms, String floor) {
    rooms.getItems().clear();
    for (Node node : database.exportNodes().values()) {
      val roomToAdd = node.getLongName();
      val listOfRooms = new LinkedList<String>();
      if (!node.getNodeType().equals("STAI") &&
          !node.getNodeType().equals("ELEV") &&
          !node.getNodeType().equals("REST") &&
          !node.getNodeType().equals("HALL") &&
          node.getFloor() == Integer.parseInt(floor)) {
        rooms.getItems().add(roomToAdd);
      }
    }
  }

  /**
   * searches the database to get the node which matches the node selected in the room dropdown
   */
  @FXML
  private void setNode() {
    for (val n : database.exportNodes().values()) {
      if (n.getLongName().equals(currentFloor.getSelectionModel().getSelectedItem())) {
        entryNode = n;
        break;
      }
    }
  }
}
