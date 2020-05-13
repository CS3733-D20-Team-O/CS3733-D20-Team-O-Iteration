package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ChangePasswordViewModel extends DialogViewModel {

  private final DatabaseWrapper database;
  private final LoginDetails login;
  private final SnackBar snackBar;
  private final Validator validator;

  @FXML
  private JFXTextField newPassword;

  private void updateEmployee(String password) {
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, login.getUsername(),
        EmployeeProperty.PASSWORD, password);
  }

  @FXML
  private void confirm() {
    if (validator.validate(newPassword)) {
      updateEmployee(newPassword.getText());
      snackBar.show("Password successfully updated");
      close();
    }
  }

  @FXML
  private void cancel() {
    close();
  }

}
