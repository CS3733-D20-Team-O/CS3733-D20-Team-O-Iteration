package edu.wpi.cs3733.d20.teamO.view_model.admin.About_Features;

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
public class AboutFloorMap extends DialogViewModel{
  private final Navigator navigator;

  @FXML
  private StackPane useFloorMap, container;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(useFloorMap, 2);
    JFXDepthManager.setDepth(container, 2);
  }

  @FXML
  private void openFloorMapEditor() {
    try {
      navigator.push(getString("accessFloorMapEditor"), "views/admin/FloorMapEditor.fxml");
    } catch (IOException e) {
      log.error("Failed to open Map Editor", e);
    }
  }
}
