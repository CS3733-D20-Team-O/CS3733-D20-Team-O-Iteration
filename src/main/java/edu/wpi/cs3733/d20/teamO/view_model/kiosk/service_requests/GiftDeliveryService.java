package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GiftDeliveryService extends ViewModelBase {

  private final DatabaseWrapper database;

  @FXML
  private Label toLabel, fromLabel, ccInfoLabel, ccExpirationLabel, ccNameLabel, emailAddressLabel,
      giftDeliveryLabel, deliveryTimeLabel, toRoomLabel, toFloorLabel, giftBoxLabel, qtyLabel, costLabel, taxLabel, totalLabel;
  @FXML
  private JFXTextField toField, fromField, ccNumberField, ccMonthField, ccYearField,
      ccSecurityField, firstNameField, middleNameField, lastNameField, emailAddressField, timeField, qtyField;
  @FXML
  private JFXButton submitButton, cancelButton;
  @FXML
  private JFXComboBox<String> inRoomComboBox, onFloorComboBox, giftComboBox, timeComboBox, hrComboBox, minComboBox;


  ArrayList<Node> listOfRooms = new ArrayList<>();

  @Override
  protected void start(URL location, ResourceBundle resources) {

    makeNodes();
    inRoomComboBox.setDisable(true);
    setupComboBoxes();
  }

  private void makeNodes() {
    if (database.exportNodes().size() > 0) {
      return;
    }
    database.addNode("Node1", 1, 1, 1, "Main", "DEPT", "Test Depart1", "testDept");
    database.addNode("Node2", 1, 1, 2, "Main", "LABS", "Test Lab2", "testLab");
    database.addNode("Node3", 1, 1, 3, "Main", "CONF", "Test CONF3", "testLab");
    database.addNode("Node4", 1, 1, 4, "Main", "HALL", "Test HALL4", "testLab");
    database.addNode("Node5", 1, 1, 5, "Main", "LABS", "Test Lab5", "testLab");
    database.addNode("Node6", 1, 1, 5, "Main", "STAI", "Test STAI5", "testLab");
  }

  //set up combo boxes
  public void setupComboBoxes() {
    System.out.println("in setupComboBoxes");
    addAvailableFloors();
    addAvailableRooms();
    onFloorComboBox.getItems().add(0, "Floors");
    onFloorComboBox.getSelectionModel().selectFirst();
    inRoomComboBox.getItems().add(0, "Rooms");
    inRoomComboBox.getSelectionModel().selectFirst();
    addAvailableTimes();
  }

  private void addAvailableTimes() {
    hrComboBox.getItems()
        .addAll("HR", "12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
    hrComboBox.getSelectionModel().selectFirst();
    minComboBox.getItems()
        .addAll("MIN", "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
    minComboBox.getSelectionModel().selectFirst();
    timeComboBox.getItems().addAll("PERIOD", "AM", "PM");
    timeComboBox.getSelectionModel().selectFirst();
  }

  private void addAvailableFloors() {
    onFloorComboBox.getItems().addAll("1", "2", "3", "4", "5");
  }

  @FXML
  private void setRoomsByFloor() {
    val currentFloor = onFloorComboBox.getSelectionModel().getSelectedItem();
    inRoomComboBox.getItems().clear();
    for (val node : listOfRooms) {
      if (Integer.toString(node.getFloor()).equals(currentFloor)) {
        inRoomComboBox.getItems().add(node.getLongName());
      }
    }
    inRoomComboBox.setDisable(false);
    inRoomComboBox.getSelectionModel().selectFirst();
//    val saveSpot = onFloorComboBox.getSelectionModel().getSelectedItem();
//    onFloorComboBox.getItems().remove(0); //removes Floors from the combobox
//    onFloorComboBox.getSelectionModel().select(saveSpot);
  }

  //rooms
  private void addAvailableRooms() {
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();
      val roomNode = node.getValue();

      if (!listOfRooms.contains(roomToAdd) &&
          node.getValue().getNodeType() != "STAI" &&
          node.getValue().getNodeType() != "ELEV" &&
          node.getValue().getNodeType() != "REST" &&
          node.getValue().getNodeType() != "HALL") {
        listOfRooms.add(roomNode);
        inRoomComboBox.getItems().add(roomToAdd);
      }//end if
    }//end for loop
  }

  private boolean checkFieldPopulated(JFXTextField tf) {
    //returns true if the textField is not empty
    return !tf.getText().isEmpty();
  }

  private boolean checkComboBoxPopulated(JFXComboBox<String> box) {
    //returns true if the selection is not Floors and Room
    System.out.println(box.getSelectionModel().getSelectedItem());
    if (!box.getSelectionModel().getSelectedItem().equals("Floors")
        && !box.getSelectionModel().getSelectedItem().equals("Rooms")) {
      return true;
    }
    if ((!onFloorComboBox.getSelectionModel().getSelectedItem().isEmpty() && inRoomComboBox
        .getSelectionModel().getSelectedItem().isEmpty())) {
      return true;
    }
    return inRoomComboBox.getSelectionModel().isEmpty();
  }

  private boolean verifyCCInfo(String ccNumber, String ccYear, String ccMonth, String ccSecurity) {
    if (ccNumberField.getText().equals("") || (ccNumber.length() <= 15 || ccNumber.length() > 19)) {
      showError("Credit Card Number is " + ccNumber.length());
      ccNumberField.requestFocus();
      ccNumberField.setFocusColor(Color.ORANGE);
      return false;
    }
    if (ccYearField.getText().equals("") || ccYear.length() != 4) {
      showError("Invalid Credit Card Year");
      ccYearField.requestFocus();
      ccYearField.setFocusColor(Color.ORANGE);
      return false;
    }
    if (ccMonthField.getText().equals("") || Integer.parseInt(ccMonth) < 1
        || Integer.parseInt(ccMonth) > 12) {
      ccMonthField.requestFocus();
      ccMonthField.setFocusColor(Color.ORANGE);
      showError("Invalid Credit Card Month");
      return false;
    }
    if (ccSecurityField.getText().equals("") || ccSecurity.length() != 3) {
      ccSecurityField.requestFocus();
      ccSecurityField.setFocusColor(Color.ORANGE);
      showError("Invalid Credit Card Security Number");
      return false;
    }
    return true;
  }

  //create service request
  private void generateRequest() {
    /*
    Integer reqIndex = database.exportServiceRequests().size();
    val requestorName = fromField.getText();
    Node requestNode = null;
    for (Node node : database.exportNodes().values()) {
      if (node.getLongName().equals(inRoomComboBox.getSelectionModel().getSelectedItem())) {
        requestNode = node;
      }
    }
    val reqNodeID = requestNode.getNodeID();
    val reqNodeType = requestNode.getNodeType();
    val reqTime = hrComboBox.getSelectionModel().getSelectedItem() + minComboBox.getSelectionModel()
        .getSelectedItem() + timeComboBox.getSelectionModel().getSelectedItem();

    if (reqIndex == 0) {
      System.out.println("In first index");
      val code = new Random().nextInt(99999999);
      database.addEmployee("", "", "", true);

      val newRequestID = new ServiceRequest(Integer.toString(code), reqTime, reqNodeID, reqNodeType,
          requestorName, "", "");

      database.addServiceRequest(newRequestID.getRequestID(), newRequestID.getRequestTime(),
          newRequestID.getRequestNode(),
          "Gift", newRequestID.getRequesterName(), newRequestID.getWhoMarked(),
          newRequestID.getEmployeeAssigned());

      System.out.println(database);

    } else {
      System.out.println("In second index");
      val currentRequestID = Integer.parseInt(database.exportServiceRequests()
          .get(database.exportServiceRequests().size() - 1).getRequestID());
      val reqIDString = Integer.toString(currentRequestID + 1);

      val newRequestID = new ServiceRequest(reqIDString, reqTime, reqNodeID, reqNodeType,
          requestorName, "", "");

      System.out.println(newRequestID);
      //requestID, requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
      database.addServiceRequest(newRequestID.getRequestID(), newRequestID.getRequestTime(),
          newRequestID.getRequestNode(),
          "Gift", newRequestID.getRequesterName(), newRequestID.getWhoMarked(),
          newRequestID.getEmployeeAssigned());

      System.out.println(database);
    }

*/
  }

  private void showError(String displayText) {
    val alert = new Alert(AlertType.ERROR);
    alert.setTitle("Missing Information");
    alert.setContentText(
        "To continue with your purchase, please fill out the following:\n" + displayText);
    alert.showAndWait();
    return;
  }


  @FXML
  private void onSubmitButton() {
    if (!verifyCCInfo(ccNumberField.getText(), ccYearField.getText(), ccMonthField.getText(),
        ccSecurityField.getText())) {
      return;
    }

    if (!checkTimePopulated(hrComboBox)) {
      showError("Hour");
      hrComboBox.requestFocus();
      hrComboBox.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkTimePopulated(minComboBox)) {
      showError("Minute");
      minComboBox.requestFocus();
      minComboBox.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkTimePopulated(timeComboBox)) {
      showError("Period");
      timeComboBox.requestFocus();
      timeComboBox.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkComboBoxPopulated(onFloorComboBox)) {
      showError("Floor");
      return;
    }
    if (!checkComboBoxPopulated(inRoomComboBox)) {
      showError("Room");
      return;
    }

    if (!checkFieldPopulated(toField)) {
      showError("To field");
      toField.requestFocus();
      toField.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkFieldPopulated(fromField)) {
      showError("From field");
      fromField.requestFocus();
      fromField.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkFieldPopulated(firstNameField)) {
      showError("First Name");
      firstNameField.requestFocus();
      firstNameField.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkFieldPopulated(lastNameField)) {
      showError("Last Name");
      lastNameField.requestFocus();
      lastNameField.setFocusColor(Color.ORANGE);
      return;
    }
    if (!checkFieldPopulated(emailAddressField)) {
      showError("email address");
      emailAddressField.requestFocus();
      emailAddressField.setFocusColor(Color.ORANGE);
      return;
    }

    generateRequest();
  }


  private boolean checkTimePopulated(JFXComboBox<String> timeBox) {
    System.out.println("selected timeBox is " + timeBox.getSelectionModel().getSelectedItem());
    return !timeBox.getSelectionModel().getSelectedItem().equals("HR") &&
        !timeBox.getSelectionModel().getSelectedItem().equals("MIN") &&
        !timeBox.getSelectionModel().getSelectedItem().equals("PERIOD");
  }

  private void onClearButton() {
    //todo for iteration 2
  }
}
