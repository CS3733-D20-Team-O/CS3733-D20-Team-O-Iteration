package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamE.onCallBeds;
import edu.wpi.cs3733.d20.teamM.AudioVisualRequest;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamP.APIController;
import java.io.IOException;
import edu.wpi.cs3733.d20.teamM.AudioVisual;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ServiceSelector extends DialogViewModel {

  private final Dialog dialog;


  @FXML
  private StackPane container;
  @FXML
  private VBox contentContainer;

  @Override
  protected void start(URL location, ResourceBundle resources) {

    // Set UI properties not set in FXML
    JFXDepthManager.setDepth(container, 2);
    JFXDepthManager.setDepth(contentContainer, 3);

  }

  @FXML
  private void openAVService() {
    try {
      AudioVisualRequest.run( 350,  100, 650, 550 ,
          null, null, null);
    } catch (Exception e) {
      log.error("Failed to open API", e);
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
      } catch (Exception e) {
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
  private void ServiceSelectorTitle() {}

  @FXML
  private void External() {}

  @FXML
  private void openOnCallBed() {
    try {
      onCallBeds.run(300,  300, 500, 450 ,
          null, null, null);
    } catch (Exception e) {
      log.error("Failed to open API", e);
    }
  }

  @FXML
  private void openFoodDelivery() {
      try {
      APIController.run( 350,  100, 650, 550 ,
         null, null, null);
    } catch (Exception e) {
      log.error("Failed to open API", e);
    }
  }
}