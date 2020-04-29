package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.GiftDeliveryRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GiftDeliveryService extends ServiceRequestBase {

  private final Validator validator;
  private final SnackBar snackbar;
  private final Dialog dialog;
  private final DatabaseWrapper database;
  ArrayList<Node> listOfRooms = new ArrayList<>();

  @FXML
  private JFXTextField toField, fromField, ccNumberField,
      ccSecurityField, ccFirstNameField, ccLastNameField, emailAddressField;
  @FXML
  private JFXComboBox<String> inRoomComboBox, onFloorComboBox, ccTypeComboBox,
      ccMonthComboBox, ccYearComboBox,
      giftComboBox;

  @FXML
  private Label totalLabel;

  @FXML
  private JFXTimePicker timePicker;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    inRoomComboBox.setDisable(true);
    addComboBoxOptions(); //adds Floors, Rooms
  }


  @FXML
  private void updateTotal() {
    if (splitItem().get(1).equals(null)) {
      return;
    }

    System.out.println(splitItem().get(0));
    totalLabel.setText("Total: $" + splitItem().get(1));
  }

  private void addComboBoxOptions() {
    onFloorComboBox.getItems().addAll("1", "2", "3", "4", "5");

    //sets rooms
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();
      val roomNode = node.getValue();
      //ignores non-valid rooms
      if (!listOfRooms.contains(roomToAdd) &&
          !(node.getValue().getNodeType().equals("STAI")) &&
          !(node.getValue().getNodeType().equals("ELEV")) &&
          !(node.getValue().getNodeType().equals("REST")) &&
          !(node.getValue().getNodeType().equals("HALL"))) {
        listOfRooms.add(roomNode);
        inRoomComboBox.getItems().add(roomToAdd);
      }
    }

    //sets CC YYYY
    val curYear = Calendar.getInstance().get(Calendar.YEAR);
    val endYearAmount = 20;
    for (int i = 0; i <= endYearAmount; i++) {
      ccYearComboBox.getItems().add(Integer.toString(curYear + i));
    }

  }

  /**
   * Only shows available rooms when a floor is selected from the Floor combo box
   */
  @FXML
  private void setRoomsByFloors() {
    val currentFloor = onFloorComboBox.getSelectionModel().getSelectedItem();
    inRoomComboBox.getItems().clear();
    for (val node : listOfRooms) {
      if (Integer.toString(node.getFloor()).equals(currentFloor)) {
        inRoomComboBox.getItems().add(node.getLongName());
      }
    }
    inRoomComboBox.setDisable(false);
    inRoomComboBox.getSelectionModel().selectFirst();
    if (inRoomComboBox.getSelectionModel().isEmpty()) {
      inRoomComboBox.setDisable(true);
    }
  }

  @FXML
  private void onSubmitPress() {
    if (!validator.validate(giftComboBox, toField, fromField,
        onFloorComboBox, inRoomComboBox, timePicker,
        ccFirstNameField, ccLastNameField,
        ccTypeComboBox, ccMonthComboBox, ccYearComboBox)) {
      dialog.showBasic("Missing Information",
          "Please fill out the form completely to continue purchase.",
          "OK");
      return;
    }

    if (!checkCard()) {
      ccNumberField.clear();
      validator.validate(ccNumberField);
      return;
    }

    if (!checkCCS()) {
      ccSecurityField.clear();
      validator.validate(ccSecurityField);
      return;
    }

    if (!checkEmail()) {
      emailAddressField.clear();
      validator.validate(emailAddressField);
      return;
    }

    generateRequest();
  }

  private void generateRequest() {
    //splitItem().get(0) is the <name> from the combo box "<name>: $<price>"
    val requestedData = new GiftDeliveryRequestData(splitItem().get(0), toField.getText());

    val confirmationCode = database.addServiceRequest(
        timePicker.getValue().toString(),
        inRoomComboBox.getSelectionModel().getSelectedItem(),
        "Gift",
        fromField.getText(),
        requestedData);

    if (confirmationCode == null) {
      snackbar.show("Failed to create the Gift Delivery Service Request");
    } else {
      dialog.showBasic("Gift Delivery Request Submitted Successfully",
          "Your confirmation code is:\n" + confirmationCode, "Close");
    }

  }


  private ArrayList<String> splitItem() {
    String selectedItem = giftComboBox.getSelectionModel().getSelectedItem();
    String[] parts = selectedItem.split(": \\$");
    val itemInfoList = new ArrayList<String>();
    itemInfoList.add(parts[0]);
    itemInfoList.add(parts[1]);
    return itemInfoList;
  }

  private boolean checkEmail() {
    val regex = "^(.+)@(.+)$";
    Pattern pattern = Pattern.compile(regex);
    return emailAddressField.getText().matches(regex);
  }

  private boolean checkCCS() {
    return (ccSecurityField.getText().length() == 3);
  }

  private boolean checkCard() {
    return (ccNumberField.getText().length() == 16);
  }

}

