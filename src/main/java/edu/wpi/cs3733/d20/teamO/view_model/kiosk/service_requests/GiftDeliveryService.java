package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GiftDeliveryService extends ViewModelBase {

  private final DatabaseWrapper database;
  private final CSVHandler csvHandler;

  @FXML
  private Label toLabel, fromLabel, ccInfoLabel, ccExpirationLabel, ccNameLabel, emailAddressLabel, giftDeliveryLabel, deliveryTimeLabel, toRoomLabel;
  @FXML
  private JFXTextField toField, fromField, ccNumberField, ccMonthField, ccYearField, ccSecurityField, firstNameField, middleNameField, lastNameField, emailAddressField;
  @FXML
  private JFXButton submitButton, cancelButton, clearButton, helpButton;
  @FXML
  private JFXComboBox<Node> inRoomComboBox, onFloorComboBox;

  /**
   * @return the width to make Panes in FlowPane cause the FlowPane to wrap to the next line
   */
  public double getFlowNewLine() {
    return Double.MAX_VALUE;
  }

  public JFXComboBox<Node> getInRoomComboBox() {
    return inRoomComboBox;
  }

  public void setInRoomComboBox(
      JFXComboBox<Node> inRoomComboBox) {
    this.inRoomComboBox = inRoomComboBox;
  }

  public JFXComboBox<Node> getOnFloorComboBox() {
    return onFloorComboBox;
  }

  public void setOnFloorComboBox(
      JFXComboBox<Node> onFloorComboBox) {
    this.onFloorComboBox = onFloorComboBox;
  }

  public void addRoomToComboBox(Node room) {
    inRoomComboBox.getItems().add(room);
  }

  public void addFloorToComboBox(Node floor) {
    onFloorComboBox.getItems().add(floor);
  }

  public CSVHandler getCsvHandler() {
    return csvHandler;
  }

  public void setCSV(String csvFileLocation) {
    csvHandler.importNodes(
        "../../../../../../../../../../../src/test/resources/edu/wpi/cs3733/d20/teamO/model/csv/PrototypeNodes.csv");
  }

  @FXML
  private void onSubmitButton() {

    //check each field is filled out
    if (checkField(toField) == checkField(fromField) == checkField(ccNumberField) == checkField(
        ccMonthField) == checkField(ccYearField) == checkField(ccSecurityField) == checkField(
        firstNameField) == checkField(middleNameField) == checkField(lastNameField) == checkField(
        emailAddressField)) {
      //send all the text fields to be verified and processed
      val to = toField.toString();
      val from = fromField.toString();
      val ccNumberString = ccNumberField.toString();
      val ccNumber = Long.parseLong(ccNumberString);
      val ccMonthString = ccMonthField.toString();
      val ccYear = Integer.parseInt(ccMonthString);
      val ccYearString = ccYearField.toString();
      val ccMonth = Integer.parseInt(ccYearString);
      val ccSecurityString = ccSecurityField.toString();
      val ccSecurity = Integer.parseInt(ccSecurityString);
      val firstName = firstNameField.toString();
      val middleName = middleNameField.toString();
      val lastName = lastNameField.toString();
      val emailAddress = emailAddressField.toString();

      if (verifyCCInfo(ccNumber, ccYear, ccMonth, ccSecurity)) {
        //once everything is correct, save to DB

        val newRequestID = generateRequestID();  //this needs to be something different.
        val requestFloorNode = onFloorComboBox.getItems().addAll();
        val requestRoomNode = inRoomComboBox.getItems().add(database
            .exportServiceRequests(); // on the right track, just need to figure out what to do here

        // this if statement will eventually be removed
        // boolean test = true;
        // if (!test) {
        // String requestID, String requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
        database
            .addServiceRequest(newRequestID, getTime(), requestRoomNode, "Gift", toField.getText(),
                "", "");
        // }
        // FIXME

        // val testRequest = new ServiceRequest(newRequestID, getTime(), requestRoomNode, "Gift", toField.getText(), "", "");
        // FIXME
        // System.out.println(testRequest);
        int test = 0;
      }
    } else {
      log.error("Please fill out all fields.");
    }
  }

  private ArrayList<String> getAvailableFloors() {
    ArrayList<String> listOfFloors = new ArrayList<String>();
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      int floorNum = node.getValue().getFloor();
      String tempString = Integer.toString(floorNum);
      if (listOfFloors.contains(node.getKey())) {

      }
    }
    database.exportNodes().values().toArray();
    return listOfFloors;
  }

  private ArrayList<String> getAvailableRooms() {
    node.getValue().getFloor()
  }

  private String generateRequestID() {
    val currentRequestID = database.exportServiceRequests()
        .get(database.exportServiceRequests().size() - 1).getRequestID();
    val currentIDInt = Integer.parseInt(currentRequestID) + 1;
    return Integer.toString(currentIDInt);
  }


  private boolean checkField(JFXTextField tf) {
    //take in a field, check the text so it's not empty
    return !tf.getText().isEmpty();
  }

  private boolean verifyCCInfo(long ccNumber, int ccYear, int ccMonth, int ccSecurity) {
    String ccNumberString = Long.toString(ccNumber);
    String ccYearString = Integer.toString(ccYear);
    String ccMonthString = Integer.toString(ccMonth);
    String ccSecurityString = Integer.toString(ccSecurity);
    if ((ccNumberString.length() < 13) || (ccNumberString.length() > 19)) {
      return (ccYearString.length() == 2) && (ccMonthString.length() == 2) && (
          ccSecurityString.length() == 3);
    }
    return true;
  }

  private String getTime() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }

  private void onClearButton() {
    //todo for iteration 2
  }
}
