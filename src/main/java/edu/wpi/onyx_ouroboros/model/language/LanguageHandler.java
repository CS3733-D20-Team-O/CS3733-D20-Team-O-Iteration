package edu.wpi.onyx_ouroboros.model.language;

import java.util.Locale;
import java.util.ResourceBundle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

@Slf4j
@RequiredArgsConstructor
public class LanguageHandler {

  /**
   * The default LanguageHandler to be used by the application
   */
  @Getter
  private static final LanguageHandler instance = new LanguageHandler(EventBus.getDefault());

  /**
   * The EventBus to use to get and send LanguageSwitchEvents
   */
  private final EventBus bus;

  /**
   * Sets the current language to be used by the application
   *
   * @param locale the locale for the language to switch to
   */
  public void setCurrentLocale(Locale locale) {
    log.info("Switching language to " + locale.getDisplayLanguage());
    val bundle = ResourceBundle.getBundle("Strings", locale);
    bus.postSticky(new LanguageSwitchEvent(bundle));
  }

  /**
   * @return the current language bundle being used to fuel the application
   */
  public ResourceBundle getCurrentLocaleBundle() {
    return bus.getStickyEvent(LanguageSwitchEvent.class).getBundle();
  }

  /**
   * @return the current locale in use by the application
   */
  public Locale getCurrentLocale() {
    return getCurrentLocaleBundle().getLocale();
  }

  /**
   * A list of supported locales
   */
  public static final Locale[] SUPPORTED_LOCALES = {
      new Locale("en"),
      new Locale("es"),
  };
}
