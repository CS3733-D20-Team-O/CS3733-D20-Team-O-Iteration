package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EmergencyButton {

  private final Dialog dialog;

  @FXML
  private void onEmergencyPressed() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/InterpreterService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

}
