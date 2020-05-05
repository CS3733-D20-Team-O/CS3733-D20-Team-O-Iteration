package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.fxml.FXML;
import javax.swing.text.html.ImageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class serviceSelector extends ViewModelBase {

  private final LanguageHandler languageHandler;
  private final DatabaseWrapper database;
  private final LoginDetails loginDetails;
  private final Validator validator;
  private final Navigator navigator;
  private final SnackBar snackBar;
  private final Dialog dialog;

  @FXML
  private ImageView imageView;
  @FXML
  private StackPane welcomeBar, container;

  @FXML
  private void openAVService() {
//    try {
//      navigator.push(getString("Audio/Visual Services"), "edu/wpi/cs3733/d20/teamO/views/kiosk/service_requests/AVService.fxml");
//    } catch (IOException e) {
//      log.error("Failed to open Service Request", e);
//    }
    try {
      dialog.showFXML(container, "views/kiosk/service_requests/AVService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
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