package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;


import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.FloristDeliveryData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
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
  private JFXComboBox<String> floors;
  @FXML
  private JFXComboBox<String> locations, bouquet;
  @FXML
  private JFXTextArea additionalNotes;


  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floors.getItems()::add);
    // Set up the populating of locations on each floor
    floors.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      locations.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(locations.getItems()::add);
      locations.getSelectionModel().select(0);
    });

  }


  @FXML
  private void submitRequest() {
    if (!validator.validate(requesterName, floors, locations, bouquet)) {
      dialog
          .showBasic("Missing information", "Please fill out form completely to continue purchase",
              "ok");
    }
    generateRequest();
    //close();
  }

  private void generateRequest() {
    val requestedData = new FloristDeliveryData(bouquet.getSelectionModel().getSelectedItem(),
        additionalNotes.getText());
//    Node requestNode = null;
////    for (Node node : database.exportNodes().values()) {
////      if (node.getLongName().equals(locations.getSelectionModel().getSelectedItem())) {
////        requestNode = node;
////        break;
////      }
////    }

    val confirmationCode = database.addServiceRequest(
        LocalDateTime.now().toString(),
        locations.getSelectionModel().getSelectedItem(), "Florist", requesterName.getText(),
        requestedData);

    if (confirmationCode == null) {
      snackBar.show("Failed to create the sanitation service request");
    } else {
      dialog.showBasic("Sanitation Request Submitted Successfully",
          "Your confirmation code is:\n" + confirmationCode, "Close");
    }

  }

}