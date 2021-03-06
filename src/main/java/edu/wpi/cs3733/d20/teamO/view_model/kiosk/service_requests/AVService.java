package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.AVRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AVService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Dialog dialog;
  private final Validator validator;
  private final SnackBar snackBar;

  @FXML
  private JFXComboBox<String> durationComboBox, serviceRequestComboBox;
  @FXML
  private JFXTextField requesterNameField;
  @FXML
  private JFXTextArea commentTextArea;
  @FXML
  private JFXTimePicker startTimePicker;
  @FXML
  private NodeSelector nodeSelector;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    nodeSelector.setNodes(database.exportNodes().values());
  }

  @FXML
  private void onSubmitPress() {
    if (!validator
        .validate(requesterNameField, nodeSelector, durationComboBox,
            serviceRequestComboBox)) {
      snackBar.show("Please fill out the form completely to make a request.");
      return;
    }

    generateRequest();
  }

  private void generateRequest() {
    val startTime = startTimePicker.getValue().toString();
    val requestData = new AVRequestData(
        serviceRequestComboBox.getSelectionModel().getSelectedItem(),
        startTime,
        durationComboBox.getSelectionModel().getSelectedItem(),
        commentTextArea.getText());

    val confirmationCode = database.addServiceRequest(startTime,
        nodeSelector.getSelectedNode().getLongName(),
        "A/V", requesterNameField.getText(), requestData);

    if (confirmationCode == null) {
      snackBar.show("Failed to create the A/V service request");
    } else {
      close();
      showRequestConfirmation(confirmationCode);
    }
  }
}
