package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ServiceRequestSelection extends ViewModelBase {

  @FXML
  private JFXComboBox<Label> serviceSelector;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    val descriptionToFXML = new HashMap<String, String>();

    serviceSelector.getItems().add(new Label("%serviceGiftDeliveryDescription"));
    serviceSelector.getSelectionModel().selectedIndexProperty()
        .addListener(((observable, oldValue, newValue) -> {
          switch (newValue.intValue()) {
            case 0:
              dispatch(new ForwardNavigationEvent(null,
                  "views/kiosk/service_requests/GiftDeliveryService.fxml"));
              break;
            default:
              log.error("Unhandled Service Request selected");
          }
        }));
  }

  // todo on lookup clicked
}
