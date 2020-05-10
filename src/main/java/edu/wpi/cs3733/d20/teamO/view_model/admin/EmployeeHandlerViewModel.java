package edu.wpi.cs3733.d20.teamO.view_model.admin;


import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmployeeHandlerViewModel extends ViewModelBase {

  private final DatabaseWrapper database;
  private final LoginDetails loginDetails;
  private final SnackBar snackBar;

  @FXML
  private TableView<Employee> employeeTable;
  @FXML
  private JFXButton addEmp, deleteEmp, updateEmp;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    employeeTable.getItems().addAll(database.exportEmployees());
  }

  /**
   * Updates the employee table and data box based on the service request selected
   */
  @FXML
  private void updateDisplays() {
    employeeTable.getItems().setAll(database.exportEmployees());
  }

  Employee getSelectedEmployee() {
    return employeeTable.getSelectionModel().getSelectedItem();
  }

}
