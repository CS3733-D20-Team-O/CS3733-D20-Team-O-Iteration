package edu.wpi.onyx_ouroboros.model.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;

/**
 * Tests LanguageHandler
 */
public class LanguageHandlerTest {

  /**
   * Tests all the registered locales and no more (only test what the app can support)
   * <p>
   * Testing non-supported locales will cause this test to fail because ResourceBundle requires a
   * corresponding .properties file for every locale that it tries to load
   */
  @Test
  public void testRegisteredLocales() {
    val handler = new LanguageHandler(new EventBus());
    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
      handler.setCurrentLocale(locale);
      assertEquals(locale, handler.getCurrentLocale());
    }
  }
}
