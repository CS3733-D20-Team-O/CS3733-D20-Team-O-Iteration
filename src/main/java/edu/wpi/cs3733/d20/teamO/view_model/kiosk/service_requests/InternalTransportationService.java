package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import lombok.val;

public class InternalTransportationService extends ViewModelBase {

  @FXML
  private JFXComboBox currentFloor;
  @FXML
  private JFXComboBox currentRoom;
  @FXML
  private JFXTextField reqName;
  @FXML
  private JFXTextField reqTime;
  @FXML
  private TabPane transportationType;
  @FXML
  private Tab assistedTransportation, unassistedTransportation;
  @FXML
  private JFXButton submit;
  @FXML
  private ToggleGroup allRadio;

  //assisted
  @FXML
  private JFXRadioButton wheelchair, bed, gurney, escort;
  @FXML
  private JFXComboBox destinationFloor, destinationRoom;

  //assisted
  @FXML
  private JFXRadioButton crutches, independentWheelchair, legScooter, mobileIV;


  @Override
  protected void start(URL location, ResourceBundle resources) {
    //todo initialize the drop downs
  }

  @FXML
  private void onSubmit() {
    val isValid = false;

    //select which fields to validate
    if (assistedTransportation.isSelected() /* && validate appropriate fields*/) {
      //set data params
    } else if (unassistedTransportation.isSelected() /* && validate appropriate fields*/) {
      //set data params
    } else {
      //snack bar error
      return;
    }

    //make database entry
  }
}
