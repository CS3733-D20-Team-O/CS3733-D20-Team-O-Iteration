package edu.wpi.cs3733.d20.teamO.view_model.admin;


import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmployeeHandlerViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private TableView<Employee> employeeTable;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    updateDisplays();
  }

  /**
   * Updates the employee table and data box based on the service request selected
   */
  @FXML
  public void updateDisplays() {
    List<Employee> tableItems = database.exportEmployees().stream()
        .filter((e) -> !e.getType().equals("start"))
        .filter((e) -> !e.getType().equals("nullEmp"))
        .collect(Collectors.toList());
    employeeTable.getItems().setAll(tableItems);
  }

  @FXML
  private void addEmployee() {
    try {
      val viewModel = (AddEmployeeViewModel) dialog
          .showFullscreenFXML("views/admin/AddEmployee.fxml");
      viewModel.init(this);
      updateDisplays();
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void deleteEmployee() {
    val emp = getSelectedEmployee();
    if (!(emp == null)) {
      database
          .deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, emp.getEmployeeID());
    } else {
      snackBar.show("Select an employee to delete");
    }
    updateDisplays();
  }

  @FXML
  private void updateEmployee() {
    try {
      if (!(getSelectedEmployee() == null)) {
        val viewModel = (UpdateEmployeeViewModel) dialog
            .showFullscreenFXML("views/admin/UpdateEmployee.fxml");
        viewModel.init(getSelectedEmployee(), this);
      } else {
        snackBar.show("Select an employee to update");
      }
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
    updateDisplays();
  }

  Employee getSelectedEmployee() {
    return employeeTable.getSelectionModel().getSelectedItem();
  }

}
