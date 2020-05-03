package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.ServiceRequestBase;
import javafx.fxml.FXML;


public class AboutViewModel extends ServiceRequestBase {

  @FXML
  private JFXButton cancelButton;

  @FXML
  private void closeWindow() {
    close();
  }
}