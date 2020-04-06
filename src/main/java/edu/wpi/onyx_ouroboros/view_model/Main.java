package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageHandler;
import javafx.scene.control.Label;

public class Main extends ViewModelBase {

  @Language(ID = "mainWelcome")
  public Label welcomeLabel;

  public void switchToEnglish() {
    LanguageHandler.switchToEnglish();
  }

  public void switchToSpanish() {
    LanguageHandler.switchToSpanish();
  }
}
