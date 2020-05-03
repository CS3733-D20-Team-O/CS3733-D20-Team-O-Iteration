package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SanitationService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;

  @FXML
  private JFXTextField requesterName;
  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private ToggleGroup levelSelection;
  @FXML
  private JFXTextArea additionalNotes;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());
  }

  @FXML
  private void submitRequest() {
    if (validator.validate(requesterName, nodeSelector)) {
      val requestData = new SanitationRequestData(
          ((JFXRadioButton) levelSelection.getSelectedToggle()).getText(),
          additionalNotes.getText());
      // Use the current time as a spill should be cleaned up immediately
      val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
      // todo use enum for sanitation string below and extract strings to Strings.properties
      val confirmationCode = database.addServiceRequest(
          time, nodeSelector.getSelectedNode().getLongName(),
          "Sanitation", requesterName.getText(), requestData);
      if (confirmationCode == null) {
        snackBar.show("Failed to create the sanitation service request");
      } else {
        close();
        showRequestConfirmation(confirmationCode);
      }
    }
  }
}
