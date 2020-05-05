package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXSlider;
import edu.wpi.cs3733.d20.teamO.events.ZoomEvent;
import javafx.fxml.FXML;

public class ZoomBar extends ViewModelBase {

  @FXML
  private JFXSlider zoomSlider;

  /**
   * Zooms out
   */
  public void zoomOut() {
    zoomSlider.decrement();
    setZoom();
  }

  /**
   * Zooms in
   */
  public void zoomIn() {
    zoomSlider.increment();
    setZoom();
  }

  /**
   * Sets the zoom
   */
  public void setZoom() {
    int adjZoom = (int) zoomSlider.getValue() / 100;
    dispatch(new ZoomEvent(adjZoom));
  }
}
