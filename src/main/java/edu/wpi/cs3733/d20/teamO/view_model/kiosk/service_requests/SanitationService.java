package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SanitationService extends ViewModelBase {

  private final DatabaseWrapper database;

  @FXML
  private ToggleGroup levelSelection;

  @FXML
  private void submitRequest() {

  }

  @FXML
  private void cancelRequest() {

  }
}
