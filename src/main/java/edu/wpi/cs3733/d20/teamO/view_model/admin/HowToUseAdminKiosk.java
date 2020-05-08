package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HowToUseAdminKiosk extends DialogViewModel{

  private final Dialog dialog;

  @FXML
  private StackPane UseThisAppAdmin, container;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(UseThisAppAdmin, 2);
    JFXDepthManager.setDepth(container, 2);
  }

  @FXML
  private void openFloorEditorInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/FloorMapEditor.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openRequestHandlerInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/RequestHandler.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openCSVInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/ImportExportCSV.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }
}
