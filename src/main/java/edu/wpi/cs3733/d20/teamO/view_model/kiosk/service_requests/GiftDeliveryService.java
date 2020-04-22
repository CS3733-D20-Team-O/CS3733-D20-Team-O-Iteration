package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
  private AnchorPane root;
  @FXML
  private Label toLabel, fromLabel, ccInfoLabel, ccExpirationLabel, ccNameLabel, emailAddressLabel, giftDeliveryLabel, deliveryTimeLabel, toRoomLabel;
  @FXML
  private JFXTextField toField, fromField, ccNumberField, ccMonthField, ccYearField, ccSecurityField, firstNameField, middleNameField, lastNameField, emailAddressField;
  @FXML
  private JFXButton submitButton, cancelButton, clearButton, helpButton;
  @FXML
//  private JFXComboBox<Node> inRoomComboBox = new JFXComboBox<>(), onFloorComboBox = new JFXComboBox<>();
  private final JFXComboBox<String> inRoomComboBox, onFloorComboBox;
//  @FXML
//  private JFXComboBox<Node> inRoomComboBox = new JFXComboBox<>(), onFloorComboBox = new JFXComboBox<>();
//  private final JFXComboBox<String> ;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    makeNodes();
    setupComboBoxes();
  }

  private void makeNodes() {
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


  }

  //floors
//    private ArrayList<String> getAvailableFloors() {
  private void addAvailableFloors() {
    System.out.println("in addAvailableFloors");
    onFloorComboBox.setPromptText("Floors");
    ObservableList<String> listOfFloors = FXCollections.observableArrayList();

    for (val node : database.exportNodes().entrySet()) {
      System.out.println(node);
      val floorToAdd = Integer.toString(node.getValue().getFloor());
      if (!listOfFloors.contains(floorToAdd)) {
        listOfFloors.add(floorToAdd);
        System.out.println(listOfFloors);
        onFloorComboBox.getItems().add(floorToAdd);
      }
    }
//    database.exportNodes().values().toArray();
//      return listOfFloors;
    //onFloorComboBox.setItems(listOfFloors);
    System.out.println(onFloorComboBox.getItems());

//    // Set the prompt text to the currently selected language
//    val currentLocale = languageHandler.getCurrentLocale();
//    languageSwitcher.setPromptText(currentLocale.getDisplayName(currentLocale));
//    // Load in the supported locales
//    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
//      languageSwitcher.getItems().add(locale.getDisplayName(locale));
//    }
//    // Add a listener to switch to the selected locale
//    languageSwitcher.getSelectionModel().selectedIndexProperty().addListener(
//        ((observableValue, oldValue, newValue) -> languageHandler
//            .setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[newValue.intValue()])));

  }

  //rooms
  private void addAvailableRooms() {
    inRoomComboBox.setPromptText("Rooms");
    val listOfRooms = new ArrayList<String>();

    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();

      if (!listOfRooms.contains(roomToAdd) &&
          node.getValue().getNodeType() != "STAI" &&
          node.getValue().getNodeType() != "ELEV" &&
          node.getValue().getNodeType() != "REST" &&
          node.getValue().getNodeType() != "HALL") {
        listOfRooms.add(roomToAdd);
        System.out.println("Room to Add is " + roomToAdd);
        inRoomComboBox.getItems().add(roomToAdd);
      }//end if
    }//end for loop
    System.out.println(inRoomComboBox.getItems());
  }

  //on submit
  //check fields
  private boolean checkField(JFXTextField tf) {
    //take in a field, check the text so it's not empty
    return !tf.getText().isEmpty();
  }

  //check comboboxes
  private boolean checkComboBox(JFXComboBox<String> box) {
    return !box.getSelectionModel().getSelectedItem().equals("Floors");
  }

  //verify CC
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

  //create service request
  private String generateRequestID() {
    val currentRequestID = database.exportServiceRequests()
        .get(database.exportServiceRequests().size() - 1).getRequestID();
    val currentIDInt = Integer.parseInt(currentRequestID) + 1;
    return Integer.toString(currentIDInt);
  }
  //add request to database
  //show request ID

  //snackbar error
  private void showErrorSnackbar(String errorText) {
    JFXSnackbar bar = new JFXSnackbar(root);
    val label = new Label(errorText);
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    // Add 16 margin and 16 padding as per material design guidelines
    container.setStyle("-fx-background-color: #323232;  -fx-background-insets: 16");
    container.setPadding(new Insets(32)); // total padding, including margin
    bar.enqueue(new SnackbarEvent(container));
  }


  /**
   * @return the width to make Panes in FlowPane cause the FlowPane to wrap to the next line
   */
  public double getFlowNewLine() {
    return Double.MAX_VALUE;
  }

//  public JFXComboBox<Node> getInRoomComboBox() {
//    return inRoomComboBox;
//  }
//
//  public void setInRoomComboBox(
//      JFXComboBox<Node> inRoomComboBox) {
//    this.inRoomComboBox = inRoomComboBox;
//  }
//
//  public JFXComboBox<Node> getOnFloorComboBox() {
//    return onFloorComboBox;
//  }
//
//  public void setOnFloorComboBox(
//      JFXComboBox<Node> onFloorComboBox) {
//    this.onFloorComboBox = onFloorComboBox;
//  }
//
//  public void addRoomToComboBox(Node room) {
//    inRoomComboBox.getItems().add(room);
//  }
//
//  public void addFloorToComboBox(Node floor) {
//    onFloorComboBox.getItems().add(floor);
//  }

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
//        val requestRoomNode = inRoomComboBox.getItems().add(database
//            .exportServiceRequests(); // on the right track, just need to figure out what to do here

        // this if statement will eventually be removed
        // boolean test = true;
        // if (!test) {
        // String requestID, String requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
//        database
//            .addServiceRequest(newRequestID, getTime(), requestRoomNode, "Gift", toField.getText(),
//                "", "");
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


  private String getTime() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }

  private void onClearButton() {
    //todo for iteration 2
  }
}
