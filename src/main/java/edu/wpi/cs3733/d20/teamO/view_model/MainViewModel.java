package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.language.Language;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import javafx.scene.control.Label;

public class MainViewModel extends ViewModelBase {

  @Language(key = "mainWelcome")
  public Label welcomeLabel;

  public void switchToEnglish() {
    LanguageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  public void switchToSpanish() {
    LanguageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[1]);
  }
}
