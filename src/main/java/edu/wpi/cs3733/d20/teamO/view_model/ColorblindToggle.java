package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.events.CSSSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ColorblindToggle extends ViewModelBase {

  private static final String normalVisionCSSPath = "/CSS/NormalColorCSS.css";
  private static final String deuteranopiaCSSPath = "/CSS/DeuteranopiaColorCSS.css";
  private static final String protanopiaCSSPath = "/CSS/ProtanopiaColorCSS.css";
  private static final String tritanopiaCSSPath = "/CSS/TritanopiaColorCSS.css";
  @FXML
  private JFXComboBox<Label> colorPicker;

  public void selectColor(ActionEvent event) {
    System.out.print("selecting color.");
    String selection = colorPicker.getSelectionModel().getSelectedItem().getText();
    String path = "";
    switch (selection) {
      default:
      case "Normal Vision":
        path = normalVisionCSSPath;
        break;
      case "Deuteranopia":
        path = deuteranopiaCSSPath;
        break;
      case "Protanopia":
        path = protanopiaCSSPath;
        break;
      case "Tritanopia":
        path = tritanopiaCSSPath;
        break;
    }
    dispatch(new CSSSwitchEvent(path));
  }
}
