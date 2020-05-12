package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AddEmployeeViewModel extends DialogViewModel {

  private final DatabaseWrapper database;
  private final SnackBar snackBar;
  private final Validator validator;
  private EmployeeHandlerViewModel theEmpVM;

  @FXML
  private JFXTextField newName, newID, newPassword;

  @FXML
  private JFXComboBox<String> types;

  public void init(EmployeeHandlerViewModel empViewMod) {
    theEmpVM = empViewMod;
  }

  private void addEmployee(String name, String id, String type, String password) {
    database.addEmployee(id, name, type, password, true);
  }

  /**
   * If ID is in database return false
   */
  private boolean checkID(String id) {
    for (val e : database.exportEmployees()) {
      if (e.getEmployeeID().equals(id)) {
        return false;
      }
    }
    return true;
  }

  @FXML
  private void confirm() {
    if (validator.validate(newName, newID, newPassword, types)) {
      if (checkID(newID.getText())) {
        addEmployee(newName.getText(), newID.getText(), types.getSelectionModel().getSelectedItem(),
            newPassword.getText());
        snackBar.show("Employee successfully added");
        theEmpVM.updateDisplays();
        newName.clear();
        newID.clear();
        newPassword.clear();
        types.getSelectionModel().clearSelection();
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
