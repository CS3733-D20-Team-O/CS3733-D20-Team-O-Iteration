package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestHandlerViewModel extends ViewModelBase {


  private final DatabaseWrapper database;
  private final CSVHandler csvHandler;
  private final List<Employee> employeeData = new LinkedList<Employee>();
  private boolean displayUnavail = false;


  @FXML
  private AnchorPane root;
  /**
   * Buttons, Checkbox, TextField
   */
  @FXML
  private final JFXButton btnAssign = new JFXButton("Assign");
  @FXML
  private final JFXButton btnExportServiceReq = new JFXButton("Export Service Request");
  @FXML
  private final JFXButton btnExportEmployee = new JFXButton("Export Employee");
  @FXML
  private JFXCheckBox cbShowUnavail;
  @FXML
  private JFXTextField serviceMarker;
  @FXML
  private JFXTextField exportServReqFilename;
  @FXML
  private JFXTextField exportEmployeeFilename;
  //Service Request Table Stuff
  @FXML
  private TableView<ServiceRequest> serviceTable;
  //put on one line
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
  @FXML
  private TableColumn<String, ServiceRequest> colEmployeeAssigned;
  @FXML
  private TableColumn<String, ServiceRequest> colServiceType;
  //Employee Table Stuff
  @FXML
  private TableColumn<String, Employee> empID;
  @FXML
  private TableColumn<String, Employee> empName;
  @FXML
  private TableColumn<String, Employee> empType;
  @FXML
  private TableColumn<String, Employee> empAvail;
  @FXML
  private TableView<Employee> employeeTable;

  /**
   * Overrides start() to assign table columns.
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {

    cbShowUnavail.setVisible(false);

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
    //database.addEmployee("TestEmp1", "TestName1", "TestType", true);
    //testProperties();

    //firstRunOnly();
    notFirstRun();
  }

  private void firstRunOnly() {
    database.addEmployee("", "", "", true); //null employee
    database
        .addNode("RHVMNode", 0, 0, 0, "RHVMENode", "RHVMType", "RequestHandlerViewModel", "RHVM");
    database.addEmployee("1000", "Paul", "Gift", true);
    database.addEmployee("1001", "Randy", "Gift", true);
    database.addEmployee("1002", "Bobo", "Interpreter", true);
    database.addEmployee("1003", "Samuel", "Interpreter", true);
    database.addEmployee("1004", "Joeann", "Wash", true);

    //adding a bunch to see if this will slow the system down.
    int end = 1000;
    for (int i = 0; i < end; i++) {
      //employeeData.add(employee5);
      val theInt = Integer.toString(i);
      boolean avail = true;
      avail = i % 2 != 0;
      database.addEmployee(theInt, "name" + theInt, "Wash", avail);
    }
    database.addServiceRequest("14", "55", "RHVMNode", "Gift", "55", "", "");
    database.addServiceRequest("15", "55", "RHVMNode", "Gift", "55", "", "");
    database.addServiceRequest("16", "55", "RHVMNode", "Interpreter", "55", "", "");
    database.addServiceRequest("17", "55", "RHVMNode", "Interpreter", "55", "", "");
    database.addServiceRequest("18", "55", "RHVMNode", "Wash", "55", "", "");
  }

  //set all the shit to default state
  private void notFirstRun() {
    serviceTable.getItems().addAll(database.exportServiceRequests());
  }


  //DELETE THIS BEFORE PUSHING
  private void testProperties() {
    //String requestID, requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
    val service1 = new ServiceRequest("14", "55", "55", "Gift", "55", "", "");
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
    val employee2 = new Employee("13", "Randy", "Gift", true);
    val employee3 = new Employee("14", "Bobo", "Interpreter", true);
    val employee4 = new Employee("15", "Samuel", "Interpreter", true);
    val employee5 = new Employee("16", "Joeann", "Wash", true);
//    employeeTable.getItems().add(employee1);
//    employeeTable.getItems().add(employee2);
//    employeeTable.getItems().add(employee3);
//    employeeTable.getItems().add(employee4);
//    employeeTable.getItems().add(employee5);

    employeeData.add(employee1);
    employeeData.add(employee2);
    employeeData.add(employee3);
    employeeData.add(employee4);
    employeeData.add(employee5);

    employeeTable.setPlaceholder(new Label("Select a Service Request to view employees"));

  }//end test method

  /**
   * Updates the employee table based on the service request selected.
   */
  @FXML
  private void updateEmployeeTable() {
    cbShowUnavail.setVisible(true);

    ObservableList<Employee> tableItems = FXCollections.observableArrayList();
    ServiceRequest req;

    if (serviceTable.getSelectionModel().getSelectedItem() != null) {
      req = serviceTable.getSelectionModel().getSelectedItem();
      for (Employee e : database.exportEmployees()) {

        if (e.getType().equals(req.getType())) {

          if (!displayUnavail && e.getIsAvailable().equals("true")) {
            tableItems.add(e);
          } else if (!displayUnavail && !e.getIsAvailable().equals("true")) {
          }   //do nothing
          else {
            tableItems.add(e);
          }
        }//end if: type check
      }//end for: employee loop

      employeeTable.setItems(tableItems);
    } //end if: serviceTable cell not empty
    else {
      employeeTable.getItems().setAll(database.exportEmployees());
    }
  }


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
   * Updates the table and database when assign button is pressed.
   */
  @FXML
  private void assignEmployee() {
    // "error" cases to do nothing
    //no serviceReq selected
    if (serviceTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Service Request selected.");
      return;
    }
    //no employee selected
    if (employeeTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Employee selected.");
      return;
    }
    //employee selected somehow does not have same type as request
    if (!employeeTable.getSelectionModel().getSelectedItem().getType()
        .equals(serviceTable.getSelectionModel().getSelectedItem().getType())) {
      showErrorSnackbar("Employee cannot complete this Service Request.");
      return;
    }
    //employee is unavailable
    if (!employeeTable.getSelectionModel().getSelectedItem().getIsAvailable().equals("true")) {
      showErrorSnackbar("Employee is unavailable.");
      return;
    }
    //no name entered into text box
    if (serviceMarker.getText().equals("")) {
      showErrorSnackbar("No name entered in Assigner Name field.");
      return;
    }

    val req = serviceTable.getSelectionModel().getSelectedItem();
    val employee = employeeTable.getSelectionModel().getSelectedItem();
    //user enters name into text field
    val markerName = serviceMarker.getText();

    //update the serviceRequest table
    val assignedService = new ServiceRequest(req.getRequestID(), req.getRequestTime(),
        req.getRequestNode(), req.getType(), req.getRequesterName(), markerName,
        employee.getName());
    serviceTable.getItems().remove(req);
    serviceTable.getItems().add(assignedService);
    database
        .update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, req.getRequestID(),
            ServiceRequestProperty.WHO_MARKED, markerName);
    database
        .update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, req.getRequestID(),
            ServiceRequestProperty.EMPLOYEE_ASSIGNED, employee.getName());
    //update Employee
    val assignedEmployee = new Employee(employee.getEmployeeID(), employee.getName(),
        employee.getType(), false);
//    employeeData.remove(employee);
//    employeeData.add(assignedEmployee);
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, employee.getEmployeeID(),
        EmployeeProperty.IS_AVAILABLE, "false");
    updateEmployeeTable();
  }


  /**
   * Shows the unavailable employees in the employee table
   */
  @FXML
  private void setCbShowUnavail() {
    displayUnavail = !displayUnavail;
    updateEmployeeTable();
  }

  @FXML
  private void exportServiceRequest() {
    if (exportServReqFilename.getText().equals("")) {
      showErrorSnackbar("No file name entered.");
      return;
    }
    csvHandler.exportServiceRequests(exportServReqFilename.getText());
  }

  @FXML
  private void exportEmployee() {
    if (exportEmployeeFilename.getText().equals("")) {
      showErrorSnackbar("No file name entered.");
      return;
    }
    csvHandler.exportEmployees(exportEmployeeFilename.getText());
  }
}
