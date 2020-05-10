package edu.wpi.cs3733.d20.teamO.view_model.kiosk;


import com.google.inject.Inject;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HowToUseKiosk extends DialogViewModel {

  private final Dialog dialog;

  @FXML
  private StackPane useThisApp, container;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(useThisApp, 2);
    JFXDepthManager.setDepth(container, 2);
  }

  @FXML
  private void openFindPathInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/about_features/AboutPathFinding.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openServiceRequestInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/about_features/AboutServiceRequests.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openLanguageAndMiscInfo() {
    try {
      dialog.showFullscreenFXML("views/kiosk/about_features/AboutMiscFeatures.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

}
