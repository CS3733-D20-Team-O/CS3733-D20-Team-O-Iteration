package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Scheduler;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
  private JFXButton decTimeButton, incTimeButton, scheduleButton;

  @FXML
  private JFXRadioButton onCallButton, refRoom1Button, refRoom2Button, refRoom3Button;

  @FXML
  private JFXTextField durationField;

  private String roomType;

  private enum RoomState{
    NONE, //default
    On_Call, Reflection_Room_1,Reflection_Room_2,Reflection_Room_3 //room types
  }



  @FXML
  private void changeHour(ActionEvent button) {
    Integer duration = fixTime();
    JFXButton pressedButton = (JFXButton) button.getSource();

    System.out.println(pressedButton.getText());
    if (duration == 0 && pressedButton.getText().equals(">")) {
      duration = duration + 1;
    }

    else if (duration == 24 && pressedButton.getText().equals("<")) {
      duration = duration - 1;
    }

    else if (duration > 0 && duration < 24) {
      if (pressedButton.getText().equals("<")) {
        duration = duration - 1;
      }
      if (pressedButton.getText().equals(">")) {
        duration = duration + 1;
      }
    }

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


  @FXML
  private void schedule(){
    System.out.println("Schedule Button pressed");
    System.out.println(LocalTime.parse(hoursComboBox.getSelectionModel().getSelectedItem()));
    //validate fields
    //check the database
      //checkDatabase();
    // if available update the databse
    //give confirmation
    // else return error

  }
  //Enum states = On Call, Reflection 1....

  private void updateState(ActionEvent radioButton){
    JFXRadioButton pressedButton = (JFXRadioButton) radioButton.getSource();
    roomType = pressedButton.getText();

    switch(roomType) {
      case "On Call":
        //Label information
        break;
      case "Reflection 1":
        //Label information
        break;
      case "Reflection 2":
        //Label information
        break;
      case "Reflection 3":
        //Label information
        break;
    }
  }

  private void checkDatabase() {
    //export current database
    val scheduleList = database.exportScheduler(); //list
    val roomList = new ArrayList<Scheduler>();

    //request id, employee id, start date, end time, room type - all strings
    //room types; "On Call", "Reflection 1",..,"...3"
    for(int i = 0; i < scheduleList.size(); i++){
      if (roomType.equals(scheduleList.get(i).getRoomType())){
        roomList.add(scheduleList.get(i));
      }
    }

    for(int i =0; i < roomList.size(); i++){
      //look at the dates
      //completely fine
      //completely wrong
      //any wrong


    }

      //room types match from selected
      //rooms available
      //time doesn't overlap
  }

  public void checkTime(){
    val reqStartTime = convertTime(); //gives time as LocalTime
    val reqEndDate = reqStartTime.plusHours(Integer.parseInt(durationField.getText()));




//    LocalDate datePart = LocalDate.parse("2013-01-02");
//    LocalTime timePart = LocalTime.parse("04:05:06");
//    LocalDateTime dt = LocalDateTime.of(datePart, timePart);
//    String startingDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
//    String startingTime = new SimpleDateFormat("hh:mm:ss").format(startTime);

        //datePicker returns yyyy/mm/dd
    val reqStartDate = datePicker.getValue().toString();
    LocalTime requestStartTime = LocalTime.parse(hoursComboBox.getSelectionModel().getSelectedItem());


    val reqEndTime = "";
  }

  private LocalTime convertTime() {
    String selectTime = hoursComboBox.getSelectionModel().getSelectedItem();
    String[] parts = selectTime.split(" ");
    Integer time = Integer.parseInt(parts[0]);
    if (parts[0].equals("12") && parts[1].equals("AM")){
      time = 0;
    }

    if (parts[1].equals("PM")){
      time = time + 12;
    }

    return LocalTime.of(time,0);

  }

  private void timeToDuration(){
    //schedulerTime
    //get start and end time
    //turn into duration
    //create a list of unavailableTimes
    //while(duration>0){
      //add items to List
      //Start time goes into list
      //starttime + 1 hr //add to the list
      //4PM
      //5PM

  }

}
