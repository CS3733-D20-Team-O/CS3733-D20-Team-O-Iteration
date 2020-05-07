package edu.wpi.cs3733.d20.teamO.view_model;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class StreetViewViewModel extends ViewModelBase {

  private static final String BASE_MAPS_URL = "https://www.google.com/maps/dir/?api=1";
  private static final String FAULKNER_COORDINATES = "";
  private static final String FAULKNER_PLACE_ID = "ChIJN4gB7Dt544kRm5DzJWfM45U";
  private static final String MAIN_CAMPUS_COORDINATES = "";
  private static final String MAIN_CAMPUS_PLACE_ID = "";
  private static final String DIRECTIONS_TO_FAULKNER_URL = BASE_MAPS_URL
      + "&origin=" + MAIN_CAMPUS_COORDINATES + "&origin_place_id=" + MAIN_CAMPUS_PLACE_ID
      + "&destination=" + FAULKNER_COORDINATES + "&destination_place_id=" + FAULKNER_PLACE_ID;
  private static final String DIRECTIONS_TO_MAIN_CAMPUS_URL = BASE_MAPS_URL
      + "&origin=" + FAULKNER_COORDINATES + "&origin_place_id=" + FAULKNER_PLACE_ID
      + "&destination=" + MAIN_CAMPUS_COORDINATES + "&destination_place_id=" + MAIN_CAMPUS_PLACE_ID;

  @FXML
  private WebView webView;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // todo remove this to force implementers to choose
    webView.getEngine().load(DIRECTIONS_TO_MAIN_CAMPUS_URL);
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
