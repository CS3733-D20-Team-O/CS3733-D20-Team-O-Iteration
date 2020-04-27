package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
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
import javafx.scene.text.TextFlow;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestHandlerViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private final CSVHandler csvHandler;

  private String adminID;
  private String adminName;

  @FXML
  private AnchorPane root;
  /**
   * Buttons, Checkbox, TextField
   */
  @FXML
  private JFXCheckBox cbShowUnavail;
  @FXML
  private JFXTextField exportServReqFilename, exportEmployeeFilename;
  @FXML
  private TextFlow stepLbl;

  @FXML
  private JFXTextArea dataSpace;

  //Service Request Table Stuff
  //Todo update the new table columns with appropriate names instead of the ones i have as a placeholder
  @FXML
  private TableView<ServiceRequest> serviceTable;
  @FXML
  private TableColumn<String, ServiceRequest> colRequestID, colRequestTime, colRequestNode,
      colResquesterName, colWhoMarked, colEmployeeAssigned, colServiceType, colServiceStatus, colServiceData;

  //Employee Table Stuff
  @FXML
  private TableColumn<String, Employee> empID, empName, empType, empAvail;
  @FXML
  private TableView<Employee> employeeTable;

  /**
   * Overrides start() to assign table columns. Sets up the text, columns, database, and the
   * dummy-admin (for iteration 1).
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    setTableColumns();
    serviceTable.getItems().addAll(database.exportServiceRequests());
    iterationOneAdminSet();
  }

  /**
   * Creates a dummy admin for iteration one This is run after the database populated/checked CALLED
   * AFTER firstRunOnly();
   */
  private void iterationOneAdminSet() {
    adminID = "990099";
    adminName = database.employeeNameFromID(adminID);

    database.addNode("TN", 1, 1, 1, "", "", "", "");
    database.addEmployee("1234", "JOOne", "Test1", true);
    database.addServiceRequest("NO", "TN", "Test1", "beezwax, nunya",
        new SanitationRequestData("Low", "it do be a spill tho"));
  }

  //Todo add data names here too
  private void setTableColumns() {
    colRequestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
    colRequestTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    colRequestNode.setCellValueFactory(new PropertyValueFactory<>("requestNode"));
    colServiceType.setCellValueFactory(new PropertyValueFactory<>("type"));
    colResquesterName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
    colWhoMarked.setCellValueFactory(new PropertyValueFactory<>("whoMarked"));
    colEmployeeAssigned.setCellValueFactory(new PropertyValueFactory<>("employeeAssigned"));
    colServiceStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    colServiceData.setCellValueFactory(new PropertyValueFactory<>("add data name"));

    empID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
    empName.setCellValueFactory(new PropertyValueFactory<>("name"));
    empType.setCellValueFactory(new PropertyValueFactory<>("type"));
    empAvail.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
  }

  /**
   * Updates the employee table based on the service request selected. Updates the data box to show
   * data
   */
  @FXML
  private void updateDisplays() {
    cbShowUnavail.setVisible(true);

    ObservableList<Employee> tableItems = FXCollections.observableArrayList();

    if (serviceTable.getSelectionModel().getSelectedItem() != null) {

      val req = serviceTable.getSelectionModel().getSelectedItem();
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
    if (criteraToAssignEmployeeMet()) {
      return;
    } //this returned an error - ending method

    val selectedRequest = serviceTable.getSelectionModel().getSelectedItem();
    val selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

    //using dummy admin value

    //update the serviceRequest table
    //Creates a new service request with the current selected service request
    //Includes the assigned employee and the admin that assigned them.
    updateServiceTable(adminID, selectedRequest, selectedEmployee);

    //if this is false there was an error in updating the database and will not update the employee.
    if (!updateDatabase(selectedEmployee, selectedRequest, adminID)) {
      System.out.println("Exiting assignEmployee() due to updateDatabase error");
      return;
    }

    System.out.println("Updating Employee Table from assignEmployee()");
    updateDisplays();
  }

  /**
   * Updates the Employee and Service Request database. The employee needs to be updated first, then
   * they can be added to the request. Finally the admin is added to the request.
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
        selectedRequest.getRequestID(), ServiceRequestProperty.WHO_MARKED, adminID);
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
        selectedRequest.getRequesterName(), adminName,
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
    if (serviceTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Service Request selected.");
      return true;
    }
    //no employee selected
    else if (employeeTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Employee selected.");
      return true;
    }
    //task is unavailable
    else if (!serviceTable.getSelectionModel().getSelectedItem().getStatus().equals("Unassigned")) {
      showErrorSnackbar("An employee cannot be assigned to this task");
      return true;
    }
    //employee selected somehow does not have same type as request
    else if (!employeeTable.getSelectionModel().getSelectedItem().getType()
        .equals(serviceTable.getSelectionModel().getSelectedItem().getType())) {
      showErrorSnackbar("Employee cannot complete this Service Request.");
      return true;
    }
    //employee is unavailable
    else if (!employeeTable.getSelectionModel().getSelectedItem().getIsAvailable()) {
      showErrorSnackbar("Employee is unavailable.");
      return true;
    }
    //if the selected service has someone assigned
    else if (!serviceTable.getSelectionModel().getSelectedItem().getEmployeeAssigned().equals("")) {
      showErrorSnackbar("An employee is already assigned to this service request");
      return true;
    } else {
      return false;
    }
  }

  public ServiceRequest getSelectedRequest() {
    return serviceTable.getSelectionModel().getSelectedItem();
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
    if (serviceTable.getSelectionModel().getSelectedItem() == null) {
      return;
    }

    val request = serviceTable.getSelectionModel().getSelectedItem();
    if (!request.getStatus().equals("Assigned") && !request.getStatus().equals("Unassigned")) {
      showErrorSnackbar("cannot modify a closed request");
      return;
    }

    //update employee if one is assigned
    if (!request.getEmployeeAssigned().isEmpty()) {
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
