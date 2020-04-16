package edu.wpi.cs3733.d20.teamO.model.language;

import edu.wpi.cs3733.d20.teamO.events.LanguageSwitchEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

@Slf4j
public class LanguageHandler {

  /**
   * @return the current language bundle being used to fuel the application
   */
  public static ResourceBundle getCurrentLocaleBundle() {
    return EventBus.getDefault().getStickyEvent(LanguageSwitchEvent.class).getBundle();
  }

  /**
   * @return the current locale in use by the application
   */
  public static Locale getCurrentLocale() {
    return getCurrentLocaleBundle().getLocale();
  }

  /**
   * Sets the current language to be used by the application
   *
   * @param locale the locale for the language to switch to
   */
  public static void setCurrentLocale(Locale locale) {
    log.info("Switching language to " + locale.getDisplayLanguage());
    val bundle = ResourceBundle.getBundle("Strings", locale);
    EventBus.getDefault().postSticky(new LanguageSwitchEvent(bundle));
  }

  /**
   * A list of supported locales
   */
  public static final Locale[] SUPPORTED_LOCALES = {
      new Locale("en"),
      new Locale("es"),
  };
}
