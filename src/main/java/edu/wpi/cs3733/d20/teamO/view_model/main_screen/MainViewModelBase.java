package edu.wpi.cs3733.d20.teamO.view_model.main_screen;

import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MainViewModelBase extends ViewModelBase {

  public abstract void onLeftButton(ActionEvent event);

  public abstract void onRightButton(ActionEvent event);

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
