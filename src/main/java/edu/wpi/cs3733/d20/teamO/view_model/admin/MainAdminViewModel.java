package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainAdminViewModel extends ViewModelBase {

  private final Navigator navigator;
  private final Dialog dialog;
  private final FXMLLoader fxmlLoader;

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
      // Clear any previously usage of the fxmlLoader
      fxmlLoader.setRoot(null);
      fxmlLoader.setController(null);
      // Show the dialog in full screen mode
      val displayedDialog = dialog.showFullscreen(fxmlLoader.load(Main.class
          .getResourceAsStream("views/admin/ImportExportCSV.fxml")));
      // Connect the dialog's back button to the actual dialog via setDialog()
      ((ImportExportCSVViewModel) fxmlLoader.getController()).setDialog(displayedDialog);
    } catch (IOException e) {
      log.error("Failed to open the import/export csv dialog", e);
    }
  }
}
