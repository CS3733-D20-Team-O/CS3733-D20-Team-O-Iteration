package edu.wpi.onyx_ouroboros.events;

import java.util.ResourceBundle;
import lombok.Value;

/**
 * An event for when the language is switched
 */
@Value
public class LanguageSwitchEvent implements Event {

  /**
   * The ResourceBundle for the new language
   */
  ResourceBundle bundle;
}
