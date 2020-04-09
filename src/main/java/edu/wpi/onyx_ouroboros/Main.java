package edu.wpi.onyx_ouroboros;

import edu.wpi.onyx_ouroboros.model.language.LanguageHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Entry point for the application
 */
@Slf4j
public class Main {
  public static void main(String[] args) {
    // Set English as default language
    LanguageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);

    // Check for launch mode (either normal or admin)
    String username = null, password = null;
    for (val arg : args) {
      if (arg.startsWith("--username=")) {
        username = arg.substring(11);
      } else if (arg.startsWith("--password=")) {
        password = arg.substring(11);
      }
    }

    // Launch application in specified mode
    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      log.info("Launching application in normal mode");
      App.launch(App.class, args);
    } else {
      log.info("Launching application in admin mode");
      // todo launch admin utility w/ username and password
    }
  }
}
