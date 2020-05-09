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
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
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
  private final LoginDetails loginDetails;

  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private JFXTextField requesterName;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());
  }

  @FXML
  public void submitRequest() {
    //Generates a request, and immediately assigns an employee to it.
    database.addEmployee("1001", "Serviceemployee", "Security", true);
    database.addEmployee("1002", "Serviceemployee2", "Security", true);
    database.addEmployee("1003", "Serviceemployee3", "Security", true);
    database.addEmployee("1004", "Serviceemployee4", "Security", true);

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
    Employee employee = null;

    for (Employee employeeToAssign : database.exportEmployees()) {
      if (employeeToAssign.getIsAvailable() && employeeToAssign.getType().equals("Security")) {
        employee = employeeToAssign;
        System.out.println("sets employee to employeeToAssign");
      }
    }

    for (ServiceRequest currentReq : database.exportServiceRequests()) {
      if (database.exportServiceRequests().contains(confirmationCode)) {
        System.out.print("updates the current request with the employee.");
        database.update(
            Table.SERVICE_REQUESTS_TABLE,
            ServiceRequestProperty.REQUEST_ID,
            currentReq.getRequestID(),
            ServiceRequestProperty.EMPLOYEE_ASSIGNED,
            employee.getEmployeeID());
        database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID,
            employee.getEmployeeID(), EmployeeProperty.IS_AVAILABLE, "false");
      }
    }


  }

  private String getEmployee() {
    // go through the database
    // find the first employee that is "Security" && isAvailable
    for (val assignEmployee : database.exportEmployees()) {
      if (assignEmployee.getType().equals("Security") && assignEmployee.getIsAvailable()) {

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

  @FXML
  public String checkLocation() {
    if (nodeSelector.getSelectedNode() == null) {
      return "Unknown Location";
    }
    return nodeSelector.getSelectedNode().getLongName();
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
