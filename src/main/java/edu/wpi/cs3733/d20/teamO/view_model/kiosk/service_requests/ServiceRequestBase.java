package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.util.function.Predicate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public abstract class ServiceRequestBase extends Dialog.DialogViewModel {

  @Inject
  private DatabaseWrapper database;
  @Inject
  private Dialog dialog;

  @Deprecated
  @Setter
  private Predicate<Node> locationFilter = n -> true;

  @Deprecated
  protected final void setLocations(JFXComboBox<Integer> floors, JFXComboBox<String> locations) {
    // Populate the floors combobox with available nodes
    database.exportNodes().values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(floors.getItems()::add);

    // Set up the populating of locations on each floor
    floors.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      locations.getItems().clear();
      database.exportNodes().values().stream()
          .filter(node -> node.getFloor() == newFloor)
          .filter(locationFilter)
          .map(Node::getLongName).sorted()
          .forEachOrdered(locations.getItems()::add);
    });
  }

  protected final void showRequestConfirmation(String confirmationCode) {
    try {
      val viewModel = (RequestConfirmationViewModel)
          dialog.showFullscreenFXML("views/kiosk/RequestConfirmation.fxml");
      viewModel.setServiceRequest(confirmationCode);
    } catch (IOException e) {
      log.error("Failed to show the detailed confirmation dialog", e);
      dialog.showBasic("Success", "Confirmation code:" + confirmationCode, "Close");
    }
  }
}
