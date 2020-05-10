package edu.wpi.cs3733.d20.teamO.view_model.admin;


import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmployeeHandlerViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private final LoginDetails loginDetails;
  private final SnackBar snackBar;
  private final Dialog dialog;
  private final FXMLLoader fxmlLoader;

  @FXML
  private TableView<Employee> employeeTable;
  @FXML
  private JFXButton addEmp, deleteEmp, updateEmp;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    updateDisplays();
  }

  /**
   * Updates the employee table and data box based on the service request selected
   */
  @FXML
  private void updateDisplays() {
    List<Employee> tableItems = database.exportEmployees().stream()
        .filter((e) -> !e.getType().equals("start"))
        .filter((e) -> !e.getType().equals("nullEmp"))
        .collect(Collectors.toList());
    employeeTable.getItems().setAll(tableItems);
  }

  @FXML
  private void addEmployee() {
    try {
      dialog.showFullscreenFXML("views/admin/AddEmployee.fxml");
      updateDisplays();
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void deleteEmployee() {
    val emp = getSelectedEmployee();
    database
        .deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, emp.getEmployeeID());
    updateDisplays();
  }

  @FXML
  private void updateEmployee() {
    try {
      val viewModel = (UpdateEmployeeViewModel) dialog
          .showFullscreenFXML("views/admin/UpdateEmployee.fxml");
      viewModel.init(getSelectedEmployee());
      updateDisplays();
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
    updateDisplays();
  }

  Employee getSelectedEmployee() {
    return employeeTable.getSelectionModel().getSelectedItem();
    //Todo figure out how to check if nothing is selected
  }

}
