package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public abstract class MainViewModelBase extends ViewModelBase {

  public abstract void onLeftButton(ActionEvent event);

  public abstract void onRightButton(ActionEvent event);

  protected void openWindow(String fxmlLocation, String stageTitle) {
    try {
      val loader = get(FXMLLoader.class);
      val root = (Parent) loader.load(Main.class.getResourceAsStream(fxmlLocation));
      val stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle(stageTitle);
      stage.show();
    } catch (IOException e) {
      log.error("Failed to open a new window from " + fxmlLocation, e);
    }
  }

  // todo change these in future to use the list of supported locales (more generic) via drop-down

  @FXML
  private void onEnglishSelected() {
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  @FXML
  private void onSpanishSelected() {
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[1]);
  }
}
