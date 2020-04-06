package edu.wpi.onyx_ouroboros.model.language;

import edu.wpi.onyx_ouroboros.model.Event;

/**
 * The language model interface describing what properties each implementer should translate
 */
public interface LanguageModel extends Event {

  /**
   * @return the welcome string on the main page
   */
  String mainWelcome();
}
