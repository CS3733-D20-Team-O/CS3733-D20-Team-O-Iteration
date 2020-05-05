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
  private void openAVService() {
    try {
      navigator.push(getString("openAVService"), "view_models/kiosk/service_requests/AVService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openAExtTransport() {
    try {
      navigator.push(getString("openAExtTransport"), "view_models/kiosk/service_requests/ExternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openIntTransport() {
    try {
      navigator.push(getString("openIntTransport"), "view_models/kiosk/service_requests/InternalTransportationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openFlorist() {
    try {
      navigator.push(getString("openFlorist"), "view_models/kiosk/service_requests/FloristDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openGiftDelivery() {
    try {
      navigator.push(getString("openGiftDelivery"), "view_models/kiosk/service_requests/GiftDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openMedicineDelivery() {
    try {
      navigator.push(getString("openMedicineDelivery"), "view_models/kiosk/service_requests/MedicineDeliveryService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openSanitation() {
    try {
      navigator.push(getString("openSanitation"), "view_models/kiosk/service_requests/SanitationService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openInterpreter() {
    try {
      navigator.push(getString("openInterpreter"), "view_models/kiosk/service_requests/InterpreterService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openInfoTech() {
    try {
      navigator.push(getString("openInfoTech"), "view_models/kiosk/service_requests/InfoTechService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

  @FXML
  private void openSecurity() {
    try {
      navigator.push(getString("openSecurity"), "view_models/kiosk/service_requests/SecurityService.fxml");
    } catch (IOException e) {
      log.error("Failed to open Service Request", e);
    }
  }

}