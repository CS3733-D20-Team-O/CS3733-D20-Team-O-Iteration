package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AboutViewModel extends DialogViewModel {

  private final Dialog dialog;

  @FXML
  private void openCredits() {
    try {
      dialog.showFullscreenFXML("views/kiosk/SoftwareUsed.fxml");
      close();
    } catch (IOException e) {
      log.error("Could not load the credits page", e);
    }
  }
}