package edu.wpi.cs3733.d20.teamO.model.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
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
    for (val locale : LanguageHandler.SUPPORTED_LOCALES) {
      LanguageHandler.setCurrentLocale(locale);
      assertEquals(locale, LanguageHandler.getCurrentLocale());
    }
  }
}
