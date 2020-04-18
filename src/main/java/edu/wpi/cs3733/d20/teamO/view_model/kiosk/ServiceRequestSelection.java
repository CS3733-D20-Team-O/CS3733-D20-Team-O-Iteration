package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ServiceRequestSelection extends ViewModelBase {

  private static final double PADDING = 20, BUTTON_PADDING = 10;

  @FXML
  private AnchorPane root;

  @FXML
  private JFXComboBox<Label> serviceSelector;

  @FXML
  private HBox header, bottomButtons;

  @FXML
  private Pane headerContainer;

  /**
   * The ViewModel of the child service request being displayed (if there is one)
   */
  private ServiceRequestBase childViewModel;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    serviceSelector.getSelectionModel().selectedIndexProperty()
        .addListener(((observable, oldValue, newValue) -> {
          switch (newValue.intValue()) {
            case 0:
              loadServiceRequest("views/kiosk/service_requests/GiftDeliveryService.fxml");
              break;
            default:
              log.error("Unhandled Service Request selected");
          }
        }));
    // To start, we only want the header displayed (centered)
    // We mess with the headerContainer so the actual header's height and width are not mutated
    AnchorPane.setLeftAnchor(headerContainer, PADDING);
    AnchorPane.setRightAnchor(headerContainer, PADDING);
    AnchorPane.setTopAnchor(headerContainer, 0.0);
    AnchorPane.setBottomAnchor(headerContainer, 0.0);
    root.getChildren().remove(bottomButtons);
  }

  private void loadServiceRequest(String fxmlLocation) {
    try {
      // Load the new view
      val loader = get(FXMLLoader.class);
      val requestRoot = (Parent) loader.load(Main.class.getResourceAsStream(fxmlLocation));
      childViewModel = loader.getController();
      root.getChildren().clear();
      // Set the header's positioning
      AnchorPane.setTopAnchor(header, PADDING);
      AnchorPane.setLeftAnchor(header, PADDING);
      AnchorPane.setRightAnchor(header, PADDING);
      // Set the request maker's positioning
      AnchorPane.setTopAnchor(requestRoot, 2 * PADDING + header.getHeight());
      AnchorPane.setLeftAnchor(requestRoot, PADDING);
      AnchorPane.setRightAnchor(requestRoot, PADDING);
      // Set the buttons' positioning
      AnchorPane.setBottomAnchor(bottomButtons, BUTTON_PADDING);
      AnchorPane.setRightAnchor(bottomButtons, BUTTON_PADDING);
      root.getChildren().addAll(header, requestRoot, bottomButtons);
    } catch (IOException e) {
      log.error("Failed to load a service request FXML", e);
    }
  }

  @FXML
  private void onCancelClicked() {
    ((Stage) root.getScene().getWindow()).close();
  }

  @FXML
  private void onSubmitClicked() {
    if (childViewModel.onSubmitPressed()) {
      ((Stage) root.getScene().getWindow()).close();
    }
  }
}
