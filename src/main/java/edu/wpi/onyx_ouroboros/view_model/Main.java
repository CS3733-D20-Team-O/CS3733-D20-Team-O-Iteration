package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageHandler;
import javafx.scene.control.Label;

public class Main extends ViewModelBase {

  @Language(key = "mainWelcome")
  public Label welcomeLabel;

  public void switchToEnglish() {
    LanguageHandler.setCurrentLanguage(LanguageHandler.supportedLocales[0]);
  }

  public void switchToSpanish() {
    LanguageHandler.setCurrentLanguage(LanguageHandler.supportedLocales[1]);
  }
}
