package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import java.util.function.Predicate;
import lombok.Setter;

public abstract class ServiceRequestBase extends Dialog.DialogViewModel {

  @Inject
  private DatabaseWrapper database;

  @Setter
  private Predicate<Node> locationFilter = n -> true;

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
}
