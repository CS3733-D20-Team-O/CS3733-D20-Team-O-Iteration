package edu.wpi.cs3733.d20.teamO.view_model;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class NavigationBar extends ViewModelBase {

  @FXML
  private AnchorPane root;

  @FXML
  private JFXButton backButton;

  @FXML
  private Label windowName;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    AnchorPane.setLeftAnchor(backButton, 0.0);
    AnchorPane.setLeftAnchor(windowName, 0.0);
    AnchorPane.setRightAnchor(windowName, 0.0);
  }

  @FXML
  private void onBackPressed() {
    // todo
  }

  public void setWindowName(String titleKey) {
    windowName.setText(get(LanguageHandler.class).getCurrentLocaleBundle().getString(titleKey));
  }
}
