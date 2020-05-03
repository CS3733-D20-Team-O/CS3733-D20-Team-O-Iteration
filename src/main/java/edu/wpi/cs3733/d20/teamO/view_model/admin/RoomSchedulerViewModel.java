package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RoomSchedulerViewModel extends ServiceRequestBase {

  private final Validator validator;
  private final SnackBar snackbar;
  private final Dialog dialog;
  private final DatabaseWrapper database;

  @FXML
  private JFXDatePicker datePicker;

  @FXML
  private JFXComboBox<String> hoursComboBox;

  @FXML
  private JFXButton decTimeButton, incTimeButton, submitbutton;

  @FXML
  private JFXRadioButton onCallButton, refRoom1Button, refRoom2Button, refRoom3Button;

  @FXML
  private JFXTextField durationField;

  @FXML
  private void changeHour(ActionEvent button) {
    Integer duration = 0;
    if (button.getSource().getClass().equals("decTimeButton")) {
      duration = Integer.parseInt(durationField.getText()) - 1;
    } else {
      duration = Integer.parseInt(durationField.getText()) + 1;
    }

    durationField.setText(Integer.toString(duration));
  }

}
