package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import java.io.IOException;
import javafx.fxml.FXML;

public class AboutViewModel extends DialogViewModel {

  @FXML
  private void openCredits() {
    try {
      dialog.showFullscreenFXML("views/kiosk/SoftwareUsed.fxml");
    } catch (IOException e) {
      log.error("Could not load the software used page", e);
    }
  }

}