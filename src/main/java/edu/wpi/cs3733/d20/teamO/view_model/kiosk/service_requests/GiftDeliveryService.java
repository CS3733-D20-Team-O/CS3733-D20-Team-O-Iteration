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
      String to = toField.toString();
      String from = fromField.toString();
      String ccNumberString = ccNumberField.toString();
      long ccNumber = Long.parseLong(ccNumberString);
      String ccMonthString = ccMonthField.toString();
      int ccYear = Integer.parseInt(ccMonthString);
      String ccYearString = ccYearField.toString();
      int ccMonth = Integer.parseInt(ccYearString);
      String ccSecurityString = ccSecurityField.toString();
      int ccSecurity = Integer.parseInt(ccSecurityString);
      String firstName = firstNameField.toString();
      String middleName = middleNameField.toString();
      String lastName = lastNameField.toString();
      String emailAddress = emailAddressField.toString();

      if (verifyCCInfo(ccNumber, ccYear, ccMonth, ccSecurity)) {
        //once everything is correct, save to DB
        // val database = get(DatabaseWrapper.class);
        // FIXME

        val newRequestID = "1";//this needs to be something different.
        // val requestRoomNode = inRoomComboBox.getItems().add() // on the right track, just need to figure out what to do here

        // this if statement will eventually be removed
        // boolean test = true;
        // if (!test) {
        // String requestID, String requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
        // database.addServiceRequest(newRequestID, getTime(), requestRoomNode, "Gift", toField.getText(), "", "");
        // }
        // FIXME

        // val testRequest = new ServiceRequest(newRequestID, getTime(), requestRoomNode, "Gift", toField.getText(), "", "");
        // FIXME
        // System.out.println(testRequest);
      }
    } else {
      log.error("Please fill out all fields.");
    }
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
