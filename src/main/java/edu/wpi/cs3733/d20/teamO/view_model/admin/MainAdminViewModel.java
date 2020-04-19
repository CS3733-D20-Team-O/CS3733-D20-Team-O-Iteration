package edu.wpi.cs3733.d20.teamO.view_model.admin;

import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;

public class MainAdminViewModel extends ViewModelBase {

  @FXML
  private void openFloorMapEditor() {
    dispatch(new ForwardNavigationEvent("Floor Map Editor",
        "views/admin/FloorMapEditor.fxml"));
  }

  @FXML
  private void openServiceRequestHandler() {
    dispatch(new ForwardNavigationEvent("Service Request Handler",
        "views/admin/RequestHandler.fxml"));
  }
}
