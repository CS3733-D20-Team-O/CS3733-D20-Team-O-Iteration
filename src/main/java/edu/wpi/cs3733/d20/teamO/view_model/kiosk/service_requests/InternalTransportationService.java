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
  private JFXComboBox<String> currentFloor;
  @FXML
  private JFXComboBox<String> currentRoom;
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
  private ToggleGroup allRadio;

  //assisted
  @FXML
  private JFXRadioButton wheelchair, bed, gurney, escort;
  @FXML
  private JFXComboBox<String> destinationFloor, destinationRoom;

  //assisted
  @FXML
  private JFXRadioButton crutches, independentWheelchair, legScooter, mobileIV;


  private Node entryNode;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    addAvailableFloors();

  }

  @FXML
  private void onSubmit() {
    InternalTransportationRequestData data;


    //select which fields to validate
    if (assistedTransportation.isSelected() && validateAssisted()) {
      data = new InternalTransportationRequestData("Assisted",
          ((RadioButton) allRadio.getSelectedToggle()).getText(),
          destinationFloor.getSelectionModel().getSelectedItem());
    } else if (unassistedTransportation.isSelected() && validateUnassisted()) {
      data = new InternalTransportationRequestData("Unassisted",
          ((RadioButton) allRadio.getSelectedToggle()).getText(), "None required");
    } else {
      return; //cancel for if no data was created;
    }
    //todo make it so data happens
    database.addServiceRequest("TEMPID", reqTime.getText(), entryNode.getNodeID(), "Int. Transport",
        reqName.getText(), "", "");
  }


  private boolean validateAssisted() {
    return (allRadio.getSelectedToggle() != null) &&
        Stream.of(currentRoom, reqName, reqTime, destinationRoom)
            .allMatch(IFXValidatableControl::validate);
  }

  private boolean validateUnassisted() {
    return (allRadio.getSelectedToggle() != null) &&
        Stream.of(currentRoom, reqTime)
            .allMatch(IFXValidatableControl::validate);
  }

  private void addAvailableFloors() {
    currentFloor.getItems().addAll("1", "2", "3", "4", "5");
    destinationFloor.getItems().addAll("1", "2", "3", "4", "5");
    currentFloor.getSelectionModel().selectFirst();
    destinationFloor.getSelectionModel().selectFirst();
    doCurrentFloor();
    doDestinationFloor();
  }

  /**
   * function to display appropriate rooms when a floor is selected in current floor
   */
  @FXML
  private void doCurrentFloor() {
    doFloor(currentRoom, currentFloor.getSelectionModel().getSelectedItem());
  }

  /**
   * function to display appropriate rooms when a floor is selected in destination floor
   */
  @FXML
  private void doDestinationFloor() {
    doFloor(destinationRoom, destinationFloor.getSelectionModel().getSelectedItem());
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
    allRadio.selectToggle(null);
  }

  @FXML
  private void setNode() {
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      if (n.getValue().getLongName().equals(currentFloor.getSelectionModel().getSelectedItem())) {
        entryNode = n.getValue();
        break;
      }
    }
  }
}
