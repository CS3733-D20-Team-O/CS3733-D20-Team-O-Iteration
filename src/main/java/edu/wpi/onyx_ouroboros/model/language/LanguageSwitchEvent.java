package edu.wpi.onyx_ouroboros.model.language;

import edu.wpi.onyx_ouroboros.model.Event;
import java.util.ResourceBundle;
import lombok.Getter;

/**
 * An event for when the language is switched
 */
public class LanguageSwitchEvent implements Event {

  /**
   * The ResourceBundle for the new language
   */
  @Getter
  private final ResourceBundle bundle;

  /**
   * Constructs a new LanguageSwitchEvent with the given ResourceBundle
   *
   * @param bundle the ResourceBundle for the new language
   */
  public LanguageSwitchEvent(ResourceBundle bundle) {
    this.bundle = bundle;
  }
}
