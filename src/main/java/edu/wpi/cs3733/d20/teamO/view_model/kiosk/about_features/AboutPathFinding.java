package edu.wpi.cs3733.d20.teamO.view_model.kiosk.About_Features;

import com.google.inject.Inject;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import edu.wpi.cs3733.d20.teamO.Navigator;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AboutPathFinding extends DialogViewModel {

  private final Navigator navigator;

  @FXML
  private StackPane usePathFind, container;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(usePathFind, 2);
    JFXDepthManager.setDepth(container, 2);
  }

  @FXML
  private void openPathFinder() {
    try {
      navigator.push(getString("mainLeftButton"), "views/kiosk/FindPath.fxml");
    } catch (IOException e) {
      log.error("Failed to open the path finder", e);
    }
  }
}
