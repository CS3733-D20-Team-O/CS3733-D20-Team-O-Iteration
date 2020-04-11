package edu.wpi.onyx_ouroboros;

import edu.wpi.onyx_ouroboros.model.data.database.DatabaseUtilities;
import edu.wpi.onyx_ouroboros.model.language.LanguageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for the application
 */
@Slf4j
public class Main {

  /**
   * Entry point to the application
   *
   * @param args the command-line provided arguments
   */
  public static void main(String[] args) {
    // If Apache Derby not correctly setup, quit
    if (!DatabaseUtilities.checkForDerby()) {
      return;
    }

    // Set English as default language
    LanguageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);

    // todo remove for iteration 1
    PrototypeApplication.launch(args);
    /*
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
     */
  }
}
