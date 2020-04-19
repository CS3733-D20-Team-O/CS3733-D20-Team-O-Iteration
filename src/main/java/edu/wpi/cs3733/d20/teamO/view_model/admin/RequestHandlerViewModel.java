package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;

public class RequestHandlerViewModel extends ViewModelBase {

  private final List<Employee> employeeData = new LinkedList<Employee>();
  private boolean displayUnavail = false;

  /**
   * Buttons, Checkbox, TextField
   */
  @FXML
  private final JFXButton btnAssign = new JFXButton("Assign");

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
  @FXML
  private JFXCheckBox cbShowUnavail;
  @FXML
  private JFXTextField serviceMarker;
  @FXML
  private TableView<ServiceRequest> serviceTable;
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

    testProperties();
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

    //adding a bunch to see if this will slow the system down.
    int end = 1000;
    for (int i = 0; i < end; i++) {
      //employeeData.add(employee5);
      val theInt = Integer.toString(i);
      boolean avail = true;
      avail = i % 2 != 0;
      employeeData.add(new Employee(theInt, "name" + theInt, "Wash", avail));
    }

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
      for (Employee e : employeeData) {

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
      employeeTable.getItems().setAll(employeeData);
    }
  }


  /**
   * Updates the table and database when assign button is pressed.
   */
  @FXML
  private void assignEmployee() {
    // "error" cases to do nothing
    //no serviceReq selected
    if (serviceTable.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    //no employee selected
    if (employeeTable.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    //employee selected somehow does not have same type as request
    if (!employeeTable.getSelectionModel().getSelectedItem().getType()
        .equals(serviceTable.getSelectionModel().getSelectedItem().getType())) {
      return;
    }
    //employee is unavailable
    if (!employeeTable.getSelectionModel().getSelectedItem().getIsAvailable().equals("true")) {
      return;
    }
    //no name entered into text box
    if (serviceMarker.getText().equals("")) {
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

    //update Employee
    val assignedEmployee = new Employee(employee.getEmployeeID(), employee.getName(),
        employee.getType(), false);
    employeeData.remove(employee);
    employeeData.add(assignedEmployee);

    //UPDATE DATABASE
    boolean test = true;
    if (!true) {
      System.out.println("In True field");
      val serviceRequestDB = get(DatabaseWrapper.class);
      serviceRequestDB
          .update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.EMPLOYEE_ASSIGNED,
              req.getRequestID(), employee.getName());
      serviceRequestDB.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.WHO_MARKED,
          req.getRequestID(), markerName);

      val employeeDB = get(DatabaseWrapper.class);
      employeeDB
          .update(Table.EMPLOYEE_TABLE, EmployeeProperty.IS_AVAILABLE, employee.getEmployeeID(),
              "false");
    }

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
    //todo : ADD BUTTON
    val fileName = "";
    get(CSVHandler.class).exportServiceRequests(fileName);
  }

  @FXML
  private void exportEmployee() {
    //todo : ADD BUTTON
    val fileName = "";
    get(CSVHandler.class).exportEmployees(fileName);
  }
}
