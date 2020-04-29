package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
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
import java.util.HashMap;
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

  @Override
  protected void start(URL location, ResourceBundle resources) {
    serviceTable.getItems().addAll(database.exportServiceRequests());
  }

  /**
   * Updates the employee table and data box based on the service request selected
   */
  @FXML
  private void updateDisplays() {
    ObservableList<Employee> tableItems = FXCollections.observableArrayList();
    if (getSelectedRequest() != null) {
      val req = getSelectedRequest();
      dataSpace.setText(req.getRequestData().getDisplayable());
      for (val e : database.exportEmployees()) {
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
    if (criteriaToAssignEmployeeMet()) {
      return;
    }

    val selectedRequest = getSelectedRequest();
    val selectedEmployee = getSelectedEmployee();

    //update the serviceRequest table
    //Creates a new service request with the current selected service request
    //Includes the assigned employee and the admin that assigned them.
    updateServiceTable(selectedRequest, selectedEmployee);

    if (updateDatabase(selectedEmployee, selectedRequest)) {
      updateDisplays();
    }
  }

  /**
   * todo listen to database updates and output their results Updates the Employee and Service
   * Request database. The employee needs to be updated first, then they can be added to the
   * request. Finally the admin is added to the request.
   *
   * @param selectedEmployee the employee being assigned to the service request
   * @param selectedRequest  the service request that has been assigned an employee.
   */
  private boolean updateDatabase(Employee selectedEmployee, ServiceRequest selectedRequest) {
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
   * @param selectedRequest  the service request currently selected in the Service Request table.
   * @param selectedEmployee the employee currently selected in the Employee table to be assigned.
   */
  private void updateServiceTable(ServiceRequest selectedRequest, Employee selectedEmployee) {
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
  private boolean criteriaToAssignEmployeeMet() {
    // Create a map of criteria to whether they are met or not
    val criteriaMap = new HashMap<String, Boolean>();
    criteriaMap.put("No service Request selected", getSelectedRequest() == null);
    criteriaMap.put("No Employee selected", getSelectedEmployee() == null);
    criteriaMap.put("An employee cannot be assigned to this task",
        !getSelectedRequest().getStatus().equals("Unassigned"));
    criteriaMap.put("Employee cannot complete this Service Request",
        !getSelectedEmployee().getType().equals(getSelectedRequest().getType()));
    criteriaMap.put("Employee is unavailable", !getSelectedEmployee().getIsAvailable());
    criteriaMap.put("An employee is already assigned to this service request",
        !getSelectedRequest().getEmployeeAssigned().equals("0"));
    for (val entry : criteriaMap.entrySet()) {
      if (entry.getValue()) {
        // todo consider showing dialog of all encountered issues at once
        snackBar.show(entry.getKey());
        return true;
      }
    }
    return false;
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
