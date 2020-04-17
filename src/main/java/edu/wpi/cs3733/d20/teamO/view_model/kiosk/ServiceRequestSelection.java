package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Value;
import lombok.val;

public class ServiceRequestSelection extends ViewModelBase {

  @FXML
  private JFXComboBox<String> serviceSelector;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    for (val service : SERVICES) {
      serviceSelector.getItems().addAll(resources.getString(service.descriptionKey));
    }
    serviceSelector.getSelectionModel().selectedIndexProperty()
        .addListener(((observable, oldValue, newValue) -> {
          // todo load the fxml into center
        }));
  }

  @FXML
  private void onCancelClicked() {
    ((Stage) serviceSelector.getScene().getWindow()).close();
  }

  @FXML
  private void onSubmitClicked() {
    if (true) { // fixme call the child ServiceRequest viewModel's onSubmitPressed()
      ((Stage) serviceSelector.getScene().getWindow()).close();
    }
  }

  /**
   * A list of usable service requests
   * <p>
   * ALL service requests must be registered here!
   */
  public static final Service[] SERVICES = {
      new Service("serviceGiftDeliveryDescription",
          "views/service_requests/GiftDeliveryService.fxml"),
  };

  /**
   * Represents a usable service
   */
  @Value
  public static class Service {

    /**
     * The key for the description of this service
     */
    String descriptionKey;
    /**
     * The fxml file location of the service request user interface
     */
    String fxmlFile;
  }
}
