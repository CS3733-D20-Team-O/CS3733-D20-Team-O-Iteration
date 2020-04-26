package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AVService {

  private final DatabaseWrapper database;

  @FXML
  private JFXButton submitButton, clearButton;
  @FXML
  private JFXComboBox<String> durationComboBox, serviceRequestComboBox, roomComboBox, floorNumberComboBox;
  @FXML
  private JFXTextField requesterNameField;
  @FXML
  private JFXTextArea commentTextArea;
  @FXML
  private Label requesterNameLabel, floorNumberLabel, roomLabel, startTimeLabel, durationLabel,
      serviceRequestLabel, commentLabel;
  @FXML
  private JFXTimePicker startTimeTimePicker;

  ArrayList<Node> listOfRooms = new ArrayList<>();

  protected void start(URL location, ResourceBundle resources) {
    setupComboBoxes();
  }

  // set up combo boxes
  public void setupComboBoxes() {
    System.out.println("in setupComboBoxes");

    // add times to duration CB
    String time = "";
    for (int i = 0; i < 60; i += 5) {
      time = time.concat(Integer.toString(i)).concat(" minutes");
      durationComboBox.getItems().add(time);
    }

    // add services to service request CB
    String[] services = {"Music", "Video Call"};
    for (String service : services) {
      durationComboBox.getItems().add(service);
    }

    // add floors to floor CB
    floorNumberComboBox.getItems().addAll("1", "2", "3", "4", "5");

    // add rooms to room CB
    for (Map.Entry<String, Node> node : database.exportNodes().entrySet()) {
      val roomToAdd = node.getValue().getLongName();
      val roomNode = node.getValue();

      if (!listOfRooms.contains(roomToAdd) &&
          !node.getValue().getNodeType().equals("STAI") &&
          !node.getValue().getNodeType().equals("ELEV") &&
          !node.getValue().getNodeType().equals("REST") &&
          !node.getValue().getNodeType().equals("HALL")) {
        listOfRooms.add(roomNode);
        roomComboBox.getItems().add(roomToAdd);
      }
    }
  }
}
