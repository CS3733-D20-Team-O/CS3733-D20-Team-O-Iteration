package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InternalTransportationService extends ViewModelBase {

  private final DatabaseWrapper database;

  @FXML
  private JFXComboBox currentFloor;
  @FXML
  private JFXComboBox currentRoom;
  @FXML
  private JFXTextField reqName;
  @FXML
  private JFXTextField reqTime;
  @FXML
  private TabPane transportationType;
  @FXML
  private Tab assistedTransportation, unassistedTransportation;
  @FXML
  private JFXButton submit;
  @FXML
  private ToggleGroup assistedToggle, unassistedToggle;

  //assisted
  @FXML
  private JFXRadioButton wheelchair, bed, gurney, escort;
  @FXML
  private JFXComboBox destinationFloor, destinationRoom;

  //assisted
  @FXML
  private JFXRadioButton crutches, independentWheelchair, legScooter, mobileIV;


  private Node entryNode;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    //set radio buttons to default
    unassistedToggle.selectToggle(crutches);
    assistedToggle.selectToggle(wheelchair);

    // Populate the current floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(currentFloor.getItems()::add);
    // Set up the populating of locations on each floor
    currentFloor.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      currentRoom.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(currentRoom.getItems()::add);
      currentRoom.getSelectionModel().select(0);
    });
    // Preselect the first floor and the first location on that floor
    if (!currentFloor.getItems().isEmpty()) {
      currentFloor.getSelectionModel().select(0);
      currentRoom.getSelectionModel().select(0);
    }

    // Populate the destination floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(destinationFloor.getItems()::add);
    // Set up the populating of locations on each floor
    destinationFloor.getSelectionModel().selectedItemProperty()
        .addListener((o, oldFloor, newFloor) -> {
          destinationRoom.getItems().clear();
          database.exportNodes().values().stream()
              .filter(node -> newFloor.equals(node.getFloor()))
              .map(Node::getLongName).sorted()
              .forEachOrdered(destinationRoom.getItems()::add);
          destinationRoom.getSelectionModel().select(0);
        });
    // Preselect the first floor and the first location on that floor
    if (!destinationFloor.getItems().isEmpty()) {
      destinationFloor.getSelectionModel().select(0);
      destinationRoom.getSelectionModel().select(0);
    }

  }

  @FXML
  private void onSubmit() {
    InternalTransportationRequestData data;

    //select which fields to validate
    if (assistedTransportation.isSelected() && validateAssisted()) {
      data = new InternalTransportationRequestData("Assisted",
          ((RadioButton) assistedToggle.getSelectedToggle()).getText(),
          destinationFloor.getSelectionModel().getSelectedItem().toString());
    } else if (unassistedTransportation.isSelected() && validateUnassisted()) {
      data = new InternalTransportationRequestData("Unassisted",
          ((RadioButton) unassistedToggle.getSelectedToggle()).getText(), "None required");
    } else {
      return; //cancel for if no data was created;
    }

    database.addServiceRequest(reqTime.getText(), entryNode.getNodeID(), "Int. Transport",
        reqName.getText(), data);
  }

  /**
   * checks if all fields needed for an assisted transport request
   *
   * @return true if request has all needed data, false otherwise
   */
  private boolean validateAssisted() {
    return (assistedToggle.getSelectedToggle() != null) &&
        Stream.of(currentRoom, reqName, reqTime, destinationRoom)
            .allMatch(IFXValidatableControl::validate);
  }

  /**
   * checks if all fields needed for an unassisted transport request
   *
   * @return true if request has all needed data, false otherwise
   */
  private boolean validateUnassisted() {
    return (unassistedToggle.getSelectedToggle() != null) &&
        Stream.of(currentRoom, reqName, reqTime)
            .allMatch(IFXValidatableControl::validate);
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
   * @param destinationRoom the combobox to populate
   * @param floor           the floor selected
   */
  private void doFloor(JFXComboBox destinationRoom, String floor) {
    destinationRoom.getItems().clear();
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();
      // roomNode = node.getValue();
      val listOfRooms = new LinkedList<String>();
      if (/*!listOfRooms.contains(roomToAdd) &&*/
          !node.getValue().getNodeType().equals("STAI") &&
              !node.getValue().getNodeType().equals("ELEV") &&
              !node.getValue().getNodeType().equals("REST") &&
              !node.getValue().getNodeType().equals("HALL") &&
              node.getValue().getFloor() == Integer.parseInt(floor)) {
        //listOfRooms.add(roomNode);
        destinationRoom.getItems().add(roomToAdd);
      }
    }
  }

  /**
   * clears the radio buttons when the tab is switched
   */
  @FXML
  private void switchTabs() {
//    unassistedToggle.selectToggle(crutches);
//    assistedToggle.selectToggle(wheelchair);
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
