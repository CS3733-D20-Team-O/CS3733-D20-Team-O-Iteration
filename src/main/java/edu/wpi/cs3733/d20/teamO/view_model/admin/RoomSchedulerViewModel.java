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
    Integer duration = fixTime();
    JFXButton pressedButton = (JFXButton) button.getSource();

    System.out.println(pressedButton.getText());
    if (duration == 0 && pressedButton.getText().equals(">")) {
      System.out.println("0 and inc pressed");
      duration = duration + 1;
    } else if (duration == 24 && pressedButton.getText().equals("<")) {
      System.out.println("24 and dec pressed");
      duration = duration - 1;
    } else if (duration > 0 && duration < 24) {
      if (pressedButton.getText().equals("<")) {
        System.out.println("dec pressed");
        duration = duration - 1;
        System.out.println(duration);
      }
      if (pressedButton.getText().equals(">")) {
        System.out.println("inc pressed");
        duration = duration + 1;
        System.out.println(duration);
      }
    }

    System.out.println("end of method");
    durationField.setText(Integer.toString(duration));
  }

  private int fixTime() {
    //if int <0 or >24, make 0, 24
    int correctDuration = Integer.parseInt(durationField.getText());
    if (correctDuration < 0) {
      correctDuration = 0;
    } else if (correctDuration > 24) {
      correctDuration = 24;
    }
    return correctDuration;
  }

}
