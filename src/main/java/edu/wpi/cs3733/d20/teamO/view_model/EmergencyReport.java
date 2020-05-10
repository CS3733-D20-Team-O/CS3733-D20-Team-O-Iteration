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
import java.time.format.DateTimeFormatter;
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
    val securityRequest = new SecurityRequestData(checkButton(), checkNotes());
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd hh:mm a"));
    val confirmationCode = database
        .addServiceRequest(time, checkLocation(), "Security", checkName(),
            securityRequest);

    assignEmployee(confirmationCode);

    showRequestConfirmation(confirmationCode);

  }

  private void assignEmployee(String confirmationCode) {
    System.out.println("assignEmployee()");
    Employee employee = null;

    for (Employee employeeToAssign : database.exportEmployees()) {
      if (employeeToAssign.getIsAvailable() && employeeToAssign.getType().equals("Security")) {
        employee = employeeToAssign;
        break;
      }
    }

    for (ServiceRequest request : database.exportServiceRequests()) {
      if (request.getRequestID().equals(confirmationCode) && employee != null) {

        //Updates the requests, using the request id of the currently selected request from this for loop
        //updates the employee assigned field with the employee assigned in the previous loop
        database.update(
            Table.SERVICE_REQUESTS_TABLE,
            ServiceRequestProperty.REQUEST_ID,
            request.getRequestID(),
            ServiceRequestProperty.EMPLOYEE_ASSIGNED,
            employee.getEmployeeID());

        //Updates the employees, using the currently assigned employee's ID
        //sets that employees availability to false - because they're assigned to the request now.
        database.update(
            Table.EMPLOYEE_TABLE,
            EmployeeProperty.EMPLOYEE_ID,
            employee.getEmployeeID(),
            EmployeeProperty.IS_AVAILABLE,
            "false");

        //updates the requests, using the request ID from the currently selected request from this for loop
        //sets the status of this request to be assigned - as an employee is now assigned to it.
        database.update(
            Table.SERVICE_REQUESTS_TABLE,
            ServiceRequestProperty.REQUEST_ID,
            request.getRequestID(),
            ServiceRequestProperty.STATUS,
            "Assigned");

        break;
      }
    }


  }


  public String checkName() {
    if (!loginDetails.getUsername().equals("")) {
      return loginDetails.getUsername();
    } else if (requesterName.getText().isEmpty()) {
      return "no name";
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

  public String checkButton() {
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
