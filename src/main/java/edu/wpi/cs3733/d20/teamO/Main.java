package edu.wpi.cs3733.d20.teamO;

import edu.wpi.cs3733.d20.teamO.model.data.database.DatabaseUtilities;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import java.util.Arrays;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Entry point for the application
 */
@Slf4j
public class Main {

  public static void main(String[] args) {
    // If Apache Derby not correctly setup, quit
    if (!DatabaseUtilities.checkForDerby()) {
      return;
    }

    // Set English as default language
    LanguageHandler.setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);

    // Check for launch mode (either normal or admin) by checking launch arguments and launch
    val loginValid = new AdminApplication.LoginDetails(Arrays.asList(args)).isValid();
    Application.launch(loginValid ? AdminApplication.class : MainApplication.class, args);
  }
}
