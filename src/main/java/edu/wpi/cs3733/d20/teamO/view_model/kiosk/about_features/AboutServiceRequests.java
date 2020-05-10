package edu.wpi.cs3733.d20.teamO.view_model.kiosk.about_features;

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
public class AboutServiceRequests extends DialogViewModel {

  private final Dialog dialog;

  @FXML
  private StackPane useServReq, container;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(useServReq, 2);
    JFXDepthManager.setDepth(container, 2);
  }
  @FXML
  public void openServiceRequestSelect() {
    try {
      dialog.showFullscreenFXML("views/kiosk/serviceSelector.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request Screen", e);
    }
  }
}
