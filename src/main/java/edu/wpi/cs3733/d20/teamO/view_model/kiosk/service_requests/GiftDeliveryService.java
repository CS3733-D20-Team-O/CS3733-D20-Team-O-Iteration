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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GiftDeliveryService extends ServiceRequestBase {

  @FXML
  private VBox root;


  private final Validator validator;
  private final SnackBar snackbar;
  private final Dialog dialog;
  private final DatabaseWrapper database;
  ArrayList<Node> listOfRooms = new ArrayList<>();
  Map<String, String> giftList_Map = new HashMap<>();

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
    makeNodes();
    makeGiftList();
    addComboBoxOptions(); //adds Floors, Rooms, CC info, and Gifts
  }

  private void makeNodes() {
    if (database.exportNodes().size() > 0) {
      return;
    } else if (database.exportEmployees().size() < 1) {
      database.addEmployee("0", "", "", true);
    }
    database.addNode("Node1", 1, 1, 1, "Main", "DEPT", "Test Depart1", "testDept");
    database.addNode("Node2", 1, 1, 2, "Main", "LABS", "Test Lab2", "testLab");
    database.addNode("Node3", 1, 1, 3, "Main", "CONF", "Test CONF3", "testLab");
    database.addNode("Node4", 1, 1, 4, "Main", "HALL", "Test HALL4", "testLab");
    database.addNode("Node5", 1, 1, 5, "Main", "LABS", "Test Lab5", "testLab");
    database.addNode("Node6", 1, 1, 5, "Main", "STAI", "Test STAI5", "testLab");
    database.addNode("Node7", 1, 1, 5, "Main", "STAI", "Test STAI6", "testLab");
    database.addNode("Node8", 1, 1, 5, "Main", "CONF", "Test CONF8", "testLab");

  }

  private void makeGiftList() {
    giftList_Map.put("Stuffed Animal", "9.99");
    giftList_Map.put("Card", "4.99");
    giftList_Map.put("Box of Chocolates", "12.99");
    giftList_Map.put("Toy", "3.99");
  }

  @FXML
  private void updateTotal() {
    totalLabel.setText("Total: " + splitItem().get(1));
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
        System.out.println(listOfRooms);
      }
    }

    //sets up Gift combo box
    for (val item : giftList_Map.entrySet()) {
      giftComboBox.getItems().add(item.getKey() + ": $" + item.getValue());
    }

    //sets CC Type
    ccTypeComboBox.getItems().addAll("Visa", "Mastercard", "AMEX", "Discover");

    //sets CC MM
    ccMonthComboBox.getItems()
        .addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

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
    clearScreen();
  }

  private void clearScreen() {
    giftComboBox.getSelectionModel().clearSelection();
    toField.clear();
    fromField.clear();
    onFloorComboBox.getSelectionModel().clearSelection();
    inRoomComboBox.getSelectionModel().clearSelection();
    ccFirstNameField.clear();
    ccLastNameField.clear();
    ccTypeComboBox.getSelectionModel().clearSelection();
    ccMonthComboBox.getSelectionModel().clearSelection();
    ccYearComboBox.getSelectionModel().clearSelection();
    ccNumberField.clear();
    ccSecurityField.clear();
    emailAddressField.clear();
    totalLabel.setText("Total: 0.00");
  }

  private void generateRequest() {
    //splitItem().get(0) is the <name> from the combo box "<name>: $<price>"
    val requestedData = new GiftDeliveryRequestData(splitItem().get(0), toField.getText());
    Node requestNode = null;
    for (Node node : database.exportNodes().values()) {
      if (node.getLongName().equals(inRoomComboBox.getSelectionModel().getSelectedItem())) {
        requestNode = node;
        break;
      }
    }

    val confirmationCode = database.addServiceRequest(
        timePicker.getValue().format(DateTimeFormatter.ofPattern("HH:mm")),
        requestNode.getNodeID(), "Gift", fromField.getText(), requestedData);

    if (confirmationCode == null) {
      snackbar.show("Failed to create the sanitation service request");
    } else {
      dialog.showBasic("Sanitation Request Submitted Successfully",
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

