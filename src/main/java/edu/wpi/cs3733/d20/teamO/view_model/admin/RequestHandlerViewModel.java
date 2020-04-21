package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestHandlerViewModel extends ViewModelBase {


  private final DatabaseWrapper database;
  private final CSVHandler csvHandler;
  private final List<Employee> employeeData = new LinkedList<Employee>();
  private boolean displayUnavail = false;

  private String adminID;
  private String adminName;

  @FXML
  private AnchorPane root;
  /**
   * Buttons, Checkbox, TextField
   */
//  @FXML
//  private JFXButton btnAssign = new JFXButton("Assign"), btnExportServiceReq = new JFXButton(
//      "Export Service Request"), btnExportEmployee = new JFXButton("Export Employee");
  @FXML
  private JFXCheckBox cbShowUnavail;
  @FXML
  private JFXTextField exportServReqFilename, exportEmployeeFilename;
  @FXML
  private TextFlow stepLbl;

  //Service Request Table Stuff
  @FXML
  protected TableView<ServiceRequest> serviceTable;
  @FXML
  private TableColumn<String, ServiceRequest> colRequestID, colRequestTime, colRequestNode,
      colResquesterName, colWhoMarked, colEmployeeAssigned, colServiceType;

  //Employee Table Stuff
  @FXML
  private TableColumn<String, Employee> empID, empName, empType, empAvail;
  @FXML
  protected TableView<Employee> employeeTable;

  /**
   * Overrides start() to assign table columns. Sets up the text, columns, database, and the
   * dummy-admin (for iteration 1).
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {

    setStepText(stepLbl);

    setTableColumns();

    notFirstRun();
    iterationOneAdminSet();
  }

  /**
   * Creates a dummy admin for iteration one This is run after the database populated/checked CALLED
   * AFTER firstRunOnly();
   */
  protected void iterationOneAdminSet() {
    adminID = "990099";
    adminName = database.employeeNameFromID(adminID);
  }

  private void setTableColumns() {
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
  }



  protected void notFirstRun() {
    serviceTable.getItems().addAll(database.exportServiceRequests());
  }


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
    if (errorCheck()) {
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
    updateEmployeeTable();
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
    try {
      database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID,
          selectedEmployee.getEmployeeID(), EmployeeProperty.IS_AVAILABLE, "false");
      database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
          selectedRequest.getRequestID(), ServiceRequestProperty.EMPLOYEE_ASSIGNED,
          selectedEmployee.getEmployeeID());
      database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID,
          selectedRequest.getRequestID(), ServiceRequestProperty.WHO_MARKED, adminID);

      return true;
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("There was an error updating the database.");
      return false;
    }

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
        selectedRequest.getRequestNode(), selectedRequest.getType(),
        selectedRequest.getRequesterName(), adminName,
        selectedEmployee.getEmployeeID());
    serviceTable.getItems().remove(selectedRequest);
    serviceTable.getItems().add(assignedService);
  }

  /**
   * Checks if certain criteria are met to assign employee Otherwise shows a specific snackbar
   * error.
   *
   * @return
   */
  private boolean errorCheck() {
    // "error" cases to do nothing
    //no serviceReq selected
    if (serviceTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Service Request selected.");
      return true;
    }
    //no employee selected
    if (employeeTable.getSelectionModel().getSelectedItem() == null) {
      showErrorSnackbar("No Employee selected.");
      return true;
    }
    //employee selected somehow does not have same type as request
    if (!employeeTable.getSelectionModel().getSelectedItem().getType()
        .equals(serviceTable.getSelectionModel().getSelectedItem().getType())) {
      showErrorSnackbar("Employee cannot complete this Service Request.");
      return true;
    }
    //employee is unavailable
    if (!employeeTable.getSelectionModel().getSelectedItem().getIsAvailable().equals("true")) {
      showErrorSnackbar("Employee is unavailable.");
      return true;
    }
    //if the selected service has someone assigned
    if (!serviceTable.getSelectionModel().getSelectedItem().getEmployeeAssigned().equals("")) {
      showErrorSnackbar("An employee is already assigned to this service request");
      return true;
    } else {
      return false;
    }
  }


  /**
   * Shows the unavailable employees in the employee table
   */
  @FXML
  private void setCbShowUnavail() {
    if (serviceTable.getSelectionModel().isEmpty()) {
      return;
    }
    displayUnavail = !displayUnavail;
    updateEmployeeTable();
  }

  /**
   * Exports the current Service Request database to a user-set filename.
   */
  @FXML
  private void exportServiceRequest() {
    if (exportServReqFilename.getText().equals("")) {
      showErrorSnackbar("No file name entered.");
      return;
    }
    csvHandler.exportServiceRequests(exportServReqFilename.getText());
  }

  /**
   * Exports the current Employee database to a user-set filename.
   */
  @FXML
  private void exportEmployee() {
    if (exportEmployeeFilename.getText().equals("")) {
      showErrorSnackbar("No file name entered.");
      return;
    }
    csvHandler.exportEmployees(exportEmployeeFilename.getText());
  }

  /**
   * Creates and sets the text for the steps to assign an employee.
   *
   * @param setTable
   */
  public void setStepText(TextFlow setTable) {
    val step1 = new Text("Step 1.");
    step1.setStyle("-fx-font-weight: bold");
    val step1txt = new Text("Select a Service Request from the Service Request table.\n");
    val step2 = new Text("Step 2.");
    step2.setStyle("-fx-font-weight: bold");
    val step2txt = new Text(
        "Select an available employee to be assigned to the request from the Employee table.\n");
    val step3 = new Text("Step 3.");
    step3.setStyle("-fx-font-weight: bold");
    val step3txt = new Text(
        "Click the Assign button to assign that employee to the request. They won't be available for other requests until it is completed.\n");
    val note = new Text("NOTE: ");
    note.setStyle("-fx-font-weight: bold");
    val notetxt = new Text(
        "Checking the Show Unavailable box also displays unavailable employees in the table.\n");

    setTable.getChildren().addAll(step1, step1txt, step2, step2txt, step3, step3txt, note, notetxt);
  }

  protected void addEmp(Employee e) {
    database.addEmployee(e.getEmployeeID(), e.getName(), e.getType(), true);
  }
}
