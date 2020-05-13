package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.EmergencyReportData;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmergencyReport extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final LoginDetails loginDetails;

  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private JFXTextField requesterName;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());
  }

  @FXML
  public void submitRequest() {
    val emergencyReport = new EmergencyReportData(checkButton(), checkNotes());
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd hh:mm a"));
    val confirmationCode = database
        .addServiceRequest(time, checkLocation(), "Emergency", checkName(),
            emergencyReport);

    showRequestConfirmation(confirmationCode);

  }

  public String checkName() {
    if (!loginDetails.getUsername().isBlank()) {
      return loginDetails.getUsername();
    } else if (requesterName.getText().isBlank()) {
      return "no name";
    }

    return requesterName.getText();
  }

  @FXML
  public String checkLocation() {
    if (nodeSelector.getSelectedNode() == null) {
      return "Unknown Location";
    }
    return nodeSelector.getSelectedNode().getLongName();
  }

  public String checkButton() {
    val checkedButton = (RadioButton) levelSelection.getSelectedToggle();

    if (levelSelection.getSelectedToggle() == null) {
      return "Unknown Emergency";
    }
    return checkedButton.getText();
  }

  public String checkNotes() {
    if (additionalNotes.getText().isBlank()) {
      return "no notes";
    }
    return additionalNotes.getText();
  }

}
