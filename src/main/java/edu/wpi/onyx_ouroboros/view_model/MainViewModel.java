package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageHandler;
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
