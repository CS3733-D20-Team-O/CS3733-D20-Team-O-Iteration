package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InternalTransportationService extends ViewModelBase {

  private final DatabaseWrapper database;

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
    InternalTransportationRequestData data;

    boolean test = Stream.of(currentRoom).allMatch(IFXValidatableControl::validate);

    //select which fields to validate
    if (assistedTransportation.isSelected() /* && validate appropriate fields*/) {
      data = new InternalTransportationRequestData("Assisted",
          ((RadioButton) allRadio.getSelectedToggle()).getText(),
          destinationFloor.getSelectionModel().getSelectedItem().toString());
    } else if (unassistedTransportation.isSelected() /* && validate appropriate fields*/) {
      data = new InternalTransportationRequestData("Unassisted",
          ((RadioButton) allRadio.getSelectedToggle()).getText(), "None required");
    } else {
      //snack bar error
      return;
    }
    //make database entry
  }
}
