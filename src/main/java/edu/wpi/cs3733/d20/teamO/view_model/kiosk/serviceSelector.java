package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.c20.teamR.AppointmentRequest;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class serviceSelector extends DialogViewModel {

  private final Dialog dialog;

  @FXML
  private StackPane ServiceSelectorTitle, container;
  @FXML
  private VBox contentContainer;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(ServiceSelectorTitle, 2);
    JFXDepthManager.setDepth(container, 2);
    JFXDepthManager.setDepth(contentContainer, 3);

  }

  @FXML
  private void openAVService() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/AVService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openAExtTransport() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/ExternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openIntTransport() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/InternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openFlorist() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/FloristDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openGiftDelivery() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/GiftDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openMedicineDelivery() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/MedicineDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openSanitation() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/SanitationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openInterpreter() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/InterpreterService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openInfoTech() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/InfoTechService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openSecurity() {
    try {
      dialog.showFullscreenFXML("views/kiosk/service_requests/SecurityService.fxml");
    } catch (IOException e) {
      log.error("Failed to open", e);
    }
  }

  @FXML
  private void openAppointmentRequest() {
    try {
      AppointmentRequest.run(300, 300, 900, 750,
          this.getClass().getResource("/CSS/default.css").toExternalForm(), null, null);
    } catch (Exception e) {
      log.error("Failed to open API", e);
    }
  }
}