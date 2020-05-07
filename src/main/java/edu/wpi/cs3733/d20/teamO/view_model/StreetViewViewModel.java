package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.effects.JFXDepthManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class StreetViewViewModel extends ViewModelBase {

  private static final String BASE_MAPS_URL = "https://www.google.com/maps/dir/?api=1";
  private static final String FAULKNER_COORDINATES = "42.304134%2C-71.125632";
  private static final String FAULKNER_PLACE_ID = "ChIJj_RG0Dt544kRuEIKrbeJBgs";
  private static final String MAIN_CAMPUS_COORDINATES = "42.335707%2C-71.105880";
  private static final String MAIN_CAMPUS_PLACE_ID = "ChIJK2GuZo5544kR-cdq-Iq9kWo";
  private static final String DIRECTIONS_TO_FAULKNER_URL = BASE_MAPS_URL
      + "&origin=" + MAIN_CAMPUS_COORDINATES + "&origin_place_id=" + MAIN_CAMPUS_PLACE_ID
      + "&destination=" + FAULKNER_COORDINATES + "&destination_place_id=" + FAULKNER_PLACE_ID;
  private static final String DIRECTIONS_TO_MAIN_CAMPUS_URL = BASE_MAPS_URL
      + "&origin=" + FAULKNER_COORDINATES + "&origin_place_id=" + FAULKNER_PLACE_ID
      + "&destination=" + MAIN_CAMPUS_COORDINATES + "&destination_place_id=" + MAIN_CAMPUS_PLACE_ID;

  @FXML
  private VBox buttonContainer;
  @FXML
  private WebView webView;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    JFXDepthManager.setDepth(buttonContainer, 3);
  }

  /**
   * Switches the directions to go to faulkner from main campus
   */
  public void goToFaulkner() {
    webView.getEngine().load(DIRECTIONS_TO_FAULKNER_URL);
  }

  /**
   * Switches the directions to go to main campus from faulkner
   */
  public void goToMainCampus() {
    webView.getEngine().load(DIRECTIONS_TO_MAIN_CAMPUS_URL);
  }
}
