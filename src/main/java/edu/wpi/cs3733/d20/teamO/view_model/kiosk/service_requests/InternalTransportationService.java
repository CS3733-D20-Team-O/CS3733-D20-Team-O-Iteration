package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class InternalTransportationService extends ViewModelBase {

  @FXML
  private JFXComboBox floor;
  @FXML
  private JFXComboBox room;
  @FXML
  private JFXTextField reqName;
  @FXML
  private JFXTextField reqTime;
  @FXML
  private TabPane transportationType;
  @FXML
  private Tab assistedTransportation, unassistedTransportation;

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

  }
}
