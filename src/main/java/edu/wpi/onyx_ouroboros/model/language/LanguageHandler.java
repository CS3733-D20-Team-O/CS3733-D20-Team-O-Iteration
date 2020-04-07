package edu.wpi.onyx_ouroboros.model.language;

import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

@Slf4j
public class LanguageHandler {

  /**
   * Sets the current language to be used by the application
   *
   * @param locale the locale for the language to switch to
   */
  public static void setCurrentLanguage(Locale locale) {
    log.info("Switching language to " + locale.getDisplayLanguage());
    val bundle = ResourceBundle.getBundle("Strings", locale);
    EventBus.getDefault().postSticky(new LanguageSwitchEvent(bundle));
  }

  /**
   * @return the current language bundle being used to fuel the application
   */
  public static ResourceBundle getCurrentLanguageBundle() {
    return EventBus.getDefault().getStickyEvent(LanguageSwitchEvent.class).getBundle();
  }

  /**
   * @return the current locale in use by the application
   */
  public static Locale getCurrentLocale() {
    return getCurrentLanguageBundle().getLocale();
  }

  /**
   * A list of supported locales
   */
  public static final Locale[] SUPPORTED_LOCALES = {
      new Locale("en"),
      new Locale("es"),
  };
}
