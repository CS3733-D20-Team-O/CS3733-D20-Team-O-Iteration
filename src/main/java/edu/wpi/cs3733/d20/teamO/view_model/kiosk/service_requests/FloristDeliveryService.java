package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;


import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.FloristDeliveryData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.model.material.node_selector.NodeSelector;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FloristDeliveryService extends ServiceRequestBase {

  private final DatabaseWrapper database;
  private final Validator validator;
  private final SnackBar snackBar;
  private final Dialog dialog;
  @FXML
  private VBox root;
  @FXML
  private JFXTextField requesterName;
  @FXML
  private NodeSelector nodeSelector;
  @FXML
  private JFXComboBox<String> bouquet;
  @FXML
  private JFXTextArea additionalNotes;


  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Populate the floors combobox with available nodes
    nodeSelector.setNodes(database.exportNodes().values());

  }


  @FXML
  private void submitRequest() {
    if (!validator.validate(requesterName, nodeSelector, bouquet)) {
      dialog
          .showBasic("Missing information", "Please fill out form completely to continue purchase",
              "ok");
    } else {
      generateRequest();
    }
  }

  private void generateRequest() {
    val requestedData = new FloristDeliveryData(bouquet.getSelectionModel().getSelectedItem(),
        additionalNotes.getText());

    val confirmationCode = database.addServiceRequest(
        LocalDateTime.now().toString(),
        nodeSelector.getSelectedNode().getLongName(), "Florist", requesterName.getText(),
        requestedData);

    if (confirmationCode == null) {
      snackBar.show("Failed to create the sanitation service request");
    } else {
      dialog.showBasic("Sanitation Request Submitted Successfully",
          "Your confirmation code is:\n" + confirmationCode, "Close");
    }
  }

}