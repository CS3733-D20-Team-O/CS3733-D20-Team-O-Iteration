package edu.wpi.cs3733.d20.teamO.model.language;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.events.LanguageSwitchEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

/**
 * Handles language related operations
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class LanguageHandler {

  /**
   * A list of supported locales
   */
  public static final Locale[] SUPPORTED_LOCALES = {
      new Locale("en"),
      new Locale("es"),
  };

  /**
   * The EventBus to use for language related operations
   */
  private final EventBus eventBus;

  /**
   * @return the current language bundle being used to fuel the application
   */
  public ResourceBundle getCurrentLocaleBundle() {
    return eventBus.getStickyEvent(LanguageSwitchEvent.class).getBundle();
  }

  /**
   * @return the current locale in use by the application
   */
  public Locale getCurrentLocale() {
    return getCurrentLocaleBundle().getLocale();
  }

  /**
   * Sets the current language to be used by the application
   *
   * @param locale the locale for the language to switch to
   */
  public void setCurrentLocale(Locale locale) {
    log.info("Switching language to " + locale.getDisplayLanguage());
    val bundle = ResourceBundle.getBundle("Strings", locale);
    eventBus.postSticky(new LanguageSwitchEvent(bundle));
  }
}
