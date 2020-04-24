package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainAdminViewModel extends ViewModelBase {

  private final Navigator navigator;

  @FXML
  private void openFloorMapEditor() {
    try {
      navigator.push("Floor Map Editor", "views/admin/FloorMapEditor.fxml");
    } catch (IOException e) {
      log.error("Failed to open the floor map editor", e);
    }
  }

  @FXML
  private void openServiceRequestHandler() {
    try {
      navigator.push("Service Request Handler", "views/admin/RequestHandler.fxml");
    } catch (IOException e) {
      log.error("Failed to open the service request handler", e);
    }
  }

  @FXML
  private void openImportExportHandler() {
    try {
      navigator.push("Import/Export CSV", "views/admin/ImportExportCSV.fxml");
    } catch (IOException e) {
      log.error("Failed to open the import/export csv wizard", e);
    }
  }
}
