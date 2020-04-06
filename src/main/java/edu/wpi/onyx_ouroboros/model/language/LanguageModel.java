package edu.wpi.onyx_ouroboros.model.language;

import edu.wpi.onyx_ouroboros.model.Event;
import org.greenrobot.eventbus.EventBus;

/**
 * The language model interface describing what properties each implementer should translate
 */
public interface LanguageModel extends Event {

  /**
   * @return the current model in use by the application
   */
  static LanguageModel getCurrentModel() {
    return EventBus.getDefault().getStickyEvent(LanguageModel.class);
  }

  // todo add language list

  /**
   * @return the welcome string on the main page
   */
  String mainWelcome();

  /**
   * @return the submit button text in the service selection
   */
  String serviceSelectionSubmitButton();

  /**
   * @return the cancel button text in the service selection
   */
  String serviceSelectionCancelButton();
}
