package edu.wpi.onyx_ouroboros.model.language;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;

@Slf4j
public class LanguageHandler {

  /**
   * Switches the application to English
   */
  public static void switchToEnglish() {
    setCurrentModel(new EnglishModel());
  }

  /**
   * Switches the application to Spanish
   */
  public static void switchToSpanish() {
    setCurrentModel(new SpanishModel());
  }

  /**
   * Sets the current language model to be used by the application
   *
   * @param languageModel the language model to use
   */
  private static void setCurrentModel(LanguageModel languageModel) {
    log.info("Switching language to " + languageModel.getClass().getCanonicalName());
    EventBus.getDefault().postSticky(languageModel);
  }

  /**
   * @return the current model in use by the application
   */
  public static LanguageModel getCurrentModel() {
    return EventBus.getDefault().getStickyEvent(LanguageModel.class);
  }

  /**
   * A class representing a language that can be registered
   */
  public static class Language {

    /**
     * The name of the language in its language Examples: English, Español, 中文
     */
    @Getter
    private final String name;
    /**
     * The runnable to call to switch to this language
     */
    @Getter
    private final Runnable switcher;

    /**
     * A constructor for a registered language
     *
     * @param name     the name of the language in the language
     * @param switcher the runnable that when called switches to this language
     */
    public Language(String name, Runnable switcher) {
      this.name = name;
      this.switcher = switcher;
    }
  }

  /**
   * A list of registered language models
   */
  public static final Language[] languageList = {
      new Language("English", LanguageHandler::switchToEnglish),
      new Language("Español", LanguageHandler::switchToSpanish),
  };
}
