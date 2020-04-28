package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestHandlerViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private final CSVHandler csvHandler;
  private final LoginDetails loginDetails;
  private final SnackBar snackBar;


  @FXML
  private JFXCheckBox cbShowUnavail;
  @FXML
  private JFXTextArea dataSpace;
  @FXML
  private TableView<ServiceRequest> serviceTable;

  @FXML
  private TableView<Employee> employeeTable;

  /**
   * Overrides start() to assign table columns. Sets up the text, columns, database
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    serviceTable.getItems().addAll(database.exportServiceRequests());
  }

  /**
   * Updates the employee table based on the service request selected. Updates the data box to show
   * data
   */
  @FXML
  private void updateDisplays() {

    ObservableList<Employee> tableItems = FXCollections.observableArrayList();

    if (getSelectedRequest() != null) {

      val req = getSelectedRequest();
      dataSpace.setText(req.getRequestData().getDisplayable());
      for (Employee e : database.exportEmployees()) {

        if (e.getType().equals(req.getType())) {

          if (!cbShowUnavail.isSelected() && e.getIsAvailable()) {
            tableItems.add(e);
          } else if (cbShowUnavail.isSelected()) {
            tableItems.add(e);
          }

        }
      }

      employeeTable.setItems(tableItems);
    }
    else {
      employeeTable.getItems().setAll(database.exportEmployees());
    }
  }

  /**
   * Updates the table and database when assign button is pressed.
   */
  @FXML
  private void assignEmployee() {
    if (criteraToAssignEmployeeMet()) {
      return;
    } //this returned an error - ending method

    val selectedRequest = getSelectedRequest();
    val selectedEmployee = getSelectedEmployee();

    //using dummy admin value

    //update the serviceRequest table
    //Creates a new service request with the current selected service request
    //Includes the assigned employee and the admin that assigned them.
    updateServiceTable(loginDetails.getUsername(), selectedRequest, selectedEmployee);

    //if this is false there was an error in updating the database and will not update the employee.
    if (!updateDatabase(selectedEmployee, selectedRequest, loginDetails.getUsername())) {
      System.out.println("Exiting assignEmployee() due to updateDatabase error");
      return;
    }

    System.out.println("Updating Employee Table from assignEmployee()");
    updateDisplays();
  }

  /**
   * todo listen to database updates and output their results Updates the Employee and Service
   * Request database. The employee needs to be updated first, then they can be added to the
   * request. Finally the admin is added to the request.
   *
   * @param selectedEmployee = the employee being assigned to the service request
   * @param selectedRequest  = the service request that has been assigned an employee.
   * @param adminID          = the person's ID that is currently logged in.
   * @return
   */
  private boolean updateDatabase(Employee selectedEmployee, ServiceRequest selectedRequest,
      String adminID) {
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID,
        selectedEmployee.getEmployeeID(), EmployeeProperty.IS_AVAILABLE, "false");
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
        selectedRequest.getRequestID(), ServiceRequestProperty.EMPLOYEE_ASSIGNED,
        selectedEmployee.getEmployeeID());
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
        selectedRequest.getRequestID(), ServiceRequestProperty.WHO_MARKED,
        loginDetails.getUsername());
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
        selectedRequest.getRequestID(), ServiceRequestProperty.STATUS, "Assigned");
    return true;
  }

  /**
   * Updates the Service Request table on the screen, after being assigned an employee.
   *
   * @param adminName        = the name of the person logged in
   * @param selectedRequest  = the service request currently selected in the Service Request table.
   * @param selectedEmployee = the employee currentled selected in the Employee table to be
   *                         assigned.
   */
  private void updateServiceTable(String adminName, ServiceRequest selectedRequest,
      Employee selectedEmployee) {
    val assignedService = new ServiceRequest(selectedRequest.getRequestID(),
        selectedRequest.getRequestTime(),
        selectedRequest.getRequestNode(), selectedRequest.getType(), "Assigned",
        selectedRequest.getRequesterName(), loginDetails.getUsername(),
        selectedEmployee.getEmployeeID(), selectedRequest.getRequestData());
    serviceTable.getItems().remove(selectedRequest);
    serviceTable.getItems().add(assignedService);
  }

  /**
   * Checks if certain criteria are met to assign employee Otherwise shows a specific snackbar
   * error.
   *
   * @return false is it is safe to add an employee
   */
  private boolean criteraToAssignEmployeeMet() {
    // "error" cases to do nothing
    //no serviceReq selected
    LinkedList<String> errors = new LinkedList<String>();

    if (getSelectedRequest() == null) {
      errors.add("No service Request selected");
    }
    //no employee selected
    else if (getSelectedEmployee() == null) {
      errors.add("No Employee selected.");
    }
    //task is unavailable
    else if (!getSelectedRequest().getStatus().equals("Unassigned")) {
      errors.add("An employee cannot be assigned to this task");
    }
    //employee selected somehow does not have same type as request
    else if (!getSelectedEmployee().getType()
        .equals(getSelectedRequest().getType())) {
      errors.add("Employee cannot complete this Service Request.");
    }
    //employee is unavailable
    else if (!getSelectedEmployee().getIsAvailable()) {
      errors.add("Employee is unavailable.");
    }
    //if the selected service has someone assigned
    else if (!getSelectedRequest().getEmployeeAssigned()
        .equals("0")) {
      errors.add("An employee is already assigned to this service request");
    }

    for (String s : errors) {
      snackBar.show(s);
    }

    return errors.size() != 0;

  }

  ServiceRequest getSelectedRequest() {
    return serviceTable.getSelectionModel().getSelectedItem();
  }

  Employee getSelectedEmployee() {
    return employeeTable.getSelectionModel().getSelectedItem();
  }


  @FXML
  private void resolveRequest() {
    finishRequest("Resolved");
  }

  @FXML
  private void cancelRequest() {
    finishRequest("Cancelled");
  }

  private void finishRequest(String input) {
    if (getSelectedRequest() == null) {
      return;
    }

    val request = getSelectedRequest();
    //if the request is not Assigned and it is not Unassigned,
    //then and only then is it able to be cancelled or resolved, so if it is equal to
    //either Assigned or Unassigned, the if fails and code continues
    if (request.getStatus().equals("Cancelled") || request.getStatus().equals("Resolved")) {
      snackBar.show("Cannot modify a closed request");
      return;
    }

    //update employee if one is assigned
    if (!request.getEmployeeAssigned().equals("0")) {
      database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID,
          request.getEmployeeAssigned(), EmployeeProperty.IS_AVAILABLE, "true");
    }
    //update service request
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
        request.getRequestID(), ServiceRequestProperty.STATUS, input);
    serviceTable.getItems().remove(request);
    serviceTable.getItems().add(new ServiceRequest(request.getRequestID(), request.getRequestTime(),
        request.getRequestNode(), request.getType(), input, request.getRequesterName(),
        request.getWhoMarked(), request.getEmployeeAssigned(), request.getRequestData()));
    updateDisplays();
  }

}
