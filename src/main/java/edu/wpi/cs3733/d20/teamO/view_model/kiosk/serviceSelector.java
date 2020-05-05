package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.fxml.FXML;
import javax.swing.text.html.ImageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;


@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class serviceSelector extends ViewModelBase {

  private final Navigator navigator;
  private final Dialog dialog;

  @FXML
  private ImageView imageView;
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
//    try {
//      navigator.push(getString("Audio/Visual Services"), "edu/wpi/cs3733/d20/teamO/views/kiosk/service_requests/AVService.fxml");
//    } catch (IOException e) {
//      log.error("Failed to open Service Request", e);
//    }
//    try {
//      imageView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
//        dialog.showFXML(AVService, "views/kiosk/service_requests/AVService.fxml");
//        event.consume();
//      });
//    } catch (IOException e) {
//      log.error("Failed to open Service Request", e);
//    }
  }

  @FXML
  private void openAExtTransport() {
    try {
      navigator.push(getString("openAExtTransport"), "views/kiosk/service_requests/ExternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openIntTransport() {
    try {
      navigator.push(getString("openIntTransport"), "views/kiosk/service_requests/InternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openFlorist() {
    try {
      navigator.push(getString("openFlorist"), "views/kiosk/service_requests/FloristDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openGiftDelivery() {
    try {
      navigator.push(getString("openGiftDelivery"), "views/kiosk/service_requests/GiftDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openMedicineDelivery() {
    try {
      navigator.push(getString("openMedicineDelivery"), "views/kiosk/service_requests/MedicineDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openSanitation() {
    try {
      navigator.push(getString("openSanitation"), "views/kiosk/service_requests/SanitationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openInterpreter() {
    try {
      navigator.push(getString("openInterpreter"), "views/kiosk/service_requests/InterpreterService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openInfoTech() {
    try {
      navigator.push(getString("openInfoTech"), "views/kiosk/service_requests/InfoTechService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openSecurity() {
    try {
      navigator.push(getString("openSecurity"), "views/kiosk/service_requests/SecurityService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }
}