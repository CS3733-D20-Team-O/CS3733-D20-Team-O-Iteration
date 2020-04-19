package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;

public class RequestHandlerViewModel extends ViewModelBase {

  @FXML
  private TableView serviceTable;
  @FXML
  private TableColumn<String, ServiceRequest> colServiceType;
  @FXML
  private TableColumn<String, ServiceRequest> colRequestID;
  @FXML
  private TableColumn<String, ServiceRequest> colRequestTime;
  @FXML
  private TableColumn<String, ServiceRequest> colRequestNode;
  @FXML
  private TableColumn<String, ServiceRequest> colResquesterName;
  @FXML
  private TableColumn<String, ServiceRequest> colWhoMarked;
  private final JFXButton btnAssign = new JFXButton("Assign");
  @FXML
  private TableColumn<String, ServiceRequest> colEmployeeAssigned;
  private final JFXCheckBox cbShowUnavail = new JFXCheckBox("Show Unavailable");
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<String, Employee> empID;
  @FXML
  private TableColumn<String, Employee> empName;
  @FXML
  private TableColumn<String, Employee> empType;
  @FXML
  private TableColumn<String, Employee> empAvail;
  //TableColumn<String, Person> firstNameColumn = TableColumn<>("First Name");
  //firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

  //TableViewSelectionModel selectionModel = serviceTable.getSelectionModel();


  @Override
  protected void start(URL location, ResourceBundle resources) {
    btnAssign.getStyleClass().add("button-raised");
    cbShowUnavail.getStyleClass().add("custom-jfx-check-box");

    colRequestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
    colRequestTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    colRequestNode.setCellValueFactory(new PropertyValueFactory<>("requestNode"));
    colServiceType.setCellValueFactory(new PropertyValueFactory<>("type"));
    colResquesterName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
    colWhoMarked.setCellValueFactory(new PropertyValueFactory<>("whoMarked"));
    colEmployeeAssigned.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));

    empID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
    empName.setCellValueFactory(new PropertyValueFactory<>("name"));
    empType.setCellValueFactory(new PropertyValueFactory<>("type"));
    empAvail.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
    //employeeTable.getColumns().addAll(empID, empName, empType, empAvail);

    testProperties();
  }

  //DELETE THIS BEFORE PUSHING
  private void testProperties() {
    //String requestID, requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
    val service1 = new ServiceRequest("14", "55", "55", "Gift", "55", "Mark", "Jimmy");
    val service2 = new ServiceRequest("14", "55", "55", "Gift", "55", "", "");
    val service3 = new ServiceRequest("14", "55", "55", "Interpreter", "55", "", "");
    val service4 = new ServiceRequest("14", "55", "55", "Interpreter", "55", "", "");
    val service5 = new ServiceRequest("14", "55", "55", "Wash", "55", "", "");
    serviceTable.getItems().add(service1);
    serviceTable.getItems().add(service2);
    serviceTable.getItems().add(service3);
    serviceTable.getItems().add(service4);
    serviceTable.getItems().add(service5);

    val employee1 = new Employee("12", "Paul", "Gift", true);
    val employee2 = new Employee("13", "Randy", "Gift", false);
    val employee3 = new Employee("14", "Bobo", "Interpreter", true);
    val employee4 = new Employee("15", "Samuel", "Interpreter", false);
    val employee5 = new Employee("16", "Joeann", "Wash", true);
    employeeTable.getItems().add(employee1);
    employeeTable.getItems().add(employee2);
    employeeTable.getItems().add(employee3);
    employeeTable.getItems().add(employee4);
    employeeTable.getItems().add(employee5);

  }

  //run whenver the tablee is clicked
  @FXML
  private void updateEmployeeTable() {
    //when the serviceTable row selected populate the employeeTable
    //get the serviceTable row serviceRequestProperty.type
    //populate the employeeTable with employees with the same type
    //check if employee isAvailable = true;
    //if not available don't show

    //val item = selectionModel.getSelectedItem();
  }

  @FXML
  private void assignEmployee() {
    //Get current serviceTable row
    //Get current employeeTable row
    //Get the employee
    //update the ServiceRequestProperty for assignedEmployee to the employee
    //set Empoyee.isAvailable = false;
    //if no employee selected this doesn't work
    //clear the employeeTable
//    serviceTable.getItems().get(0);

  }

  private void setCbShowUnavail() {
    //when box is pressed
    //if checked=true
    //set to checked=false
    //update employeeTable with just service Type
  }

  @FXML
  private void exportServiceRequest() {
    val fileName = "";
    get(CSVHandler.class).exportServiceRequests(fileName);
  }

  @FXML
  private void exportEmployee() {
    val fileName = "";
    get(CSVHandler.class).exportEmployees(fileName);
  }
  //export serviceRequst and employeeList to a file
  //choose file name in a textfield
  //get(CSVHandler.class).export<thing>("filename");

  /*
    nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xCoord.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    yCoord.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));

private void addAll(List<PrototypeNode> nodes) {
  ObservableList<PrototypeNode> displayTableItems = FXCollections.observableArrayList();
  displayTableItems.addAll(nodes);
  nodeDisplayTable.setItems(displayTableItems);
}

  @Subscribe
  public void onNodeDeleted(DatabaseNodeDeletedEvent event) {
    val nodeToDelete = event.getNodeID();

    for (val item : nodeDisplayTable.getItems()) {

      if (item.getNodeID().equals(nodeToDelete)) {
        nodeDisplayTable.getItems().remove(item);
        break;
      }
    }//end of tableLength for loop
  }

  @Subscribe
  public void onNodeInsert(DatabaseNodeInsertedEvent event) {
    nodeDisplayTable.getItems().add(event.getNode());
  }

  @Subscribe
  public void onNodeUpdated(DatabaseNodeUpdatedEvent event) {
    int i = 0;
    for (val item : nodeDisplayTable.getItems()) {
      if (item.getNodeID().equals(event.getNodeID())) {
        nodeDisplayTable.getItems().set(i, event.getNode());
        break;
      }
      i++; //gets database index
    }
  }

   */

}
