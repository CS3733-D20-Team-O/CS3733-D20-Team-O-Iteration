package edu.wpi.cs3733.d20.teamO.events;

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
