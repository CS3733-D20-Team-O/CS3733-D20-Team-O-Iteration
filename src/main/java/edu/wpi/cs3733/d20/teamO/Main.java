package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.LanguageSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import edu.wpi.cs3733.d20.teamO.injection_modules.DatabaseModule;
import edu.wpi.cs3733.d20.teamO.injection_modules.EventBusModule;
import edu.wpi.cs3733.d20.teamO.injection_modules.FXMLLoaderModule;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseUtilities;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Slf4j
public class Main extends Application {

  /**
   * A list of the registered ViewModelBases to forward events to
   */
  private final List<WeakReference<ViewModelBase>> registeredViewModels = new LinkedList<>();

  /**
   * The injector used in Applications that extend this class. Automatically passed to ViewModels
   */
  private final Injector injector = Guice.createInjector(
      new EventBusModule(),
      new FXMLLoaderModule(),
      new DatabaseModule()
  );

  /**
   * Application entry point
   *
   * @param args the command line arguments that can contain the username and password
   */
  public static void main(String[] args) {
    // If Apache Derby is correctly setup, proceed
    if (DatabaseUtilities.checkForDerby()) {
      launch(args);
    }
  }

  @Override
  public void init() {
    log.info("Starting up the application");
    // Register this application to receive events
    get(EventBus.class).register(this);
    // Setup the navigator to use the correct main screen fxml by checking login validity
    val loginDetails = get(LoginDetails.class);
    loginDetails.setFromParameters(getParameters().getRaw());
    val isValid = loginDetails
        .isValid(); // todo make load main page and main page pops up admin options
    get(Navigator.class).setMainFXML(isValid ? "views/admin/Main.fxml" : "views/kiosk/Main.fxml");
    // Set English as the default language (and trigger the loading of the main screen)
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Hospital Kiosk Application");
    primaryStage.setScene(new Scene(get(Navigator.class).getRoot()));
    primaryStage.show();
  }

  @Override
  public void stop() throws SQLException {
    log.info("Stopping the application");
    // Unregister this application from receiving any more events
    get(EventBus.class).unregister(this);
    // Close the database connection
    get(Connection.class).close();
  }

  /**
   * Creates objects of the specified type
   *
   * @param tClass the class of the object to create
   * @param <T>    the type of object to create
   * @return the new object of the specified type
   */
  private <T> T get(Class<T> tClass) {
    return injector.getInstance(tClass);
  }

  /**
   * When a language event is fired, reload the main screen to be in the new language
   *
   * @param event the event that was fired
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onLanguageSwitch(LanguageSwitchEvent event) {
    try {
      get(Navigator.class).empty();
    } catch (IOException e) {
      log.error("Failed to reload the main screen on a language switch");
    }
  }

  /**
   * Registers a ViewModel to receive events
   *
   * @param event the RegisterViewModelEvent that contains the ViewModel to register
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void registerViewModel(RegisterViewModelEvent event) {
    registeredViewModels.add(new WeakReference<>(event.getViewModel()));
  }

  /**
   * When an event is fired, forward it on to all ViewModelBases registered
   * <p>
   * This is a workaround for the lack of registerWeak in EventBus
   *
   * @param event the event that was fired
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onEvent(Event event) {
    for (var i = registeredViewModels.listIterator(); i.hasNext(); ) {
      val viewModel = i.next().get();
      // if the object was garbage collected, remove it from the list; otherwise, forward event
      if (viewModel == null) {
        i.remove();
      } else {
        Platform.runLater(() -> viewModel.onEvent(event));
      }
    }
  }
}
