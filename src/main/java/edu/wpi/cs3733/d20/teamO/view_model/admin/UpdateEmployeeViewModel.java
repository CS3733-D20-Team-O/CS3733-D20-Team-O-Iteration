package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class UpdateEmployeeViewModel extends DialogViewModel {

  private final DatabaseWrapper database;
  private final SnackBar snackBar;
  private final Validator validator;
  private String oldEmpID;
  private EmployeeHandlerViewModel theEmpVM;

  @FXML
  private JFXTextField newName, newID;

  @FXML
  private JFXComboBox<String> types;

  public void init(Employee emp, EmployeeHandlerViewModel empViewMod) {
    oldEmpID = emp.getEmployeeID();
    String name = emp.getName();
    String type = emp.getType();
    theEmpVM = empViewMod;

    newName.setText(name);
    newID.setText(oldEmpID);
    types.getSelectionModel().select(type);
  }

  private void updateEmployee(String name, String id, String type) {
    database
        .update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, oldEmpID, EmployeeProperty.NAME,
            name);
    database
        .update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, oldEmpID, EmployeeProperty.TYPE,
            type);
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, oldEmpID,
        EmployeeProperty.EMPLOYEE_ID, id);
  }

  /**
   * If ID is in database return false
   */
  private boolean checkID(String id) {
    if (!id.equals(oldEmpID)) {
      for (val e : database.exportEmployees()) {
        if (e.getEmployeeID().equals(id)) {
          return false;
        }
      }
    }
    return true;
  }

  @FXML
  private void confirm() {
    if (validator.validate(newName, newID, types)) {
      if (checkID(newID.getText())) {
        updateEmployee(newName.getText(), newID.getText(),
            types.getSelectionModel().getSelectedItem());
        snackBar.show("Employee successfully updated");
        theEmpVM.updateDisplays();
      } else {
        snackBar.show("Employee ID already exists in database");
      }
    }
  }

  @FXML
  private void cancel() {
    close();
  }


}
