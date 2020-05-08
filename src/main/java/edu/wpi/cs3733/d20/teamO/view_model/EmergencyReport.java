package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmergencyReport extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final SnackBar snackBar;
  private final LoginDetails loginDetails;

  @FXML
  private JFXTextField requesterName, floors, locations;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @FXML
  public void submitRequest() {
    //Generates a request, and immediately assigns an employee to it.
    val assignedEmployee = getEmployee();
    val securityRequest = new SecurityRequestData(checkBox(), checkNotes());
    val confirmationCode = database
        .addServiceRequest(LocalDateTime.now().toString(), checkLocation(), "Security", checkName(),
            securityRequest);

    assignEmployee(confirmationCode);

    showRequestConfirmation(confirmationCode);
//    val confirmationCode = database.addServiceRequest(
//        time, nodeSelector.getSelectedNode().getLongName(),
//        "Sanitation", requesterName.getText(), requestData);
  }

  private void assignEmployee(String confirmationCode) {
    val currentRequest = database.exportServiceRequests();
    Employee employee = new Employee("", "", "Security", true);

    for (Employee employeeToAssign : database.exportEmployees()) {
      if (employeeToAssign.getIsAvailable() && employeeToAssign.getType().equals("Security")) {
        employee = employeeToAssign;
      }
    }

    for (ServiceRequest currentReq : database.exportServiceRequests()) {
      if (database.exportServiceRequests().contains(confirmationCode)) {
        database.update(
            Table.SERVICE_REQUESTS_TABLE,
            ServiceRequestProperty.REQUEST_ID,
            currentReq.getRequestID(),
            ServiceRequestProperty.EMPLOYEE_ASSIGNED,
            employee.getEmployeeID());
      }
    }


  }

  private String getEmployee() {
    // go through the database
    // find the first employee that is "Security" && isAvailable
    for (val assignEmployee : database.exportEmployees()) {
      if (assignEmployee.getType().equals("Security") && assignEmployee.getIsAvailable()) {
        database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID,
            assignEmployee.getEmployeeID(), EmployeeProperty.IS_AVAILABLE, "false");
        return assignEmployee.getName();
      }
    }
    return "unassigned";
  }

  public String checkName() {
    if (requesterName.getText().isEmpty()) {
      return "no name";
    } else if (!loginDetails.getUsername().equals("")) {
      return loginDetails.getUsername();
    }
    return requesterName.getText();
  }

  public String checkLocation() {
    if (locations.getText().equals("")) {
      //return SecurityServiceRequestViewModel.placementNode;
    }
    return locations.getText();
  }

  public String checkBox() {
    val checkedButton = (RadioButton) levelSelection.getSelectedToggle();

    if (checkedButton.getText().equals("unknownButton")) {
      return "Unknown Emergency";
    }
    return ((RadioButton) levelSelection.getSelectedToggle()).getText();
  }

  public String checkNotes() {
    if (additionalNotes.getText().equals("")) {
      return "no notes";
    }
    return additionalNotes.getText();
  }

}
