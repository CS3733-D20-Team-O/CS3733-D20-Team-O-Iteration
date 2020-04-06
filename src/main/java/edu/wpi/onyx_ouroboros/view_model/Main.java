package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.languages.EnglishModel;
import edu.wpi.onyx_ouroboros.model.language.languages.SpanishModel;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;

public class Main extends ViewModelBase {

  @Language(ID = "mainWelcome")
  public Label welcomeLabel;

  public void switchToEnglish() {
    EventBus.getDefault().postSticky(new EnglishModel());
  }

  public void switchToSpanish() {
    EventBus.getDefault().postSticky(new SpanishModel());
  }
}
