package edu.wpi.cs3733.d20.teamO;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import edu.wpi.cs3733.d20.teamO.model.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseUtilities;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.view_model.main_screen.AdminViewModel;
import edu.wpi.cs3733.d20.teamO.view_model.main_screen.KioskViewModel;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Slf4j
// todo try @Value to remove all the finals
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

  // todo make a root view to switch views with (anchor the navigation bar at the top)

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
  final public void init() {
    log.info("Starting up the application");
    get(EventBus.class).register(this);
    // Set English as the default language
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // First, determine what mode to start in
    val loginValid = new LoginDetails(getParameters().getRaw()).isValid();

    // Setup the loader for the correct mode
    val loader = get(FXMLLoader.class);
    if (loginValid) {
      loader.setController(get(AdminViewModel.class));
    } else {
      loader.setController(get(KioskViewModel.class));
    }

    // Load and display the application
    val root = (Parent) loader.load(getClass().getResourceAsStream("views/Main.fxml"));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
  final public void stop() throws SQLException {
    log.info("Stopping the application");
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
  final protected <T> T get(Class<T> tClass) {
    return injector.getInstance(tClass);
  }

  /**
   * Navigates to a new window within the application
   *
   * @param event the navigation event
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  final public void onNavigationEvent(ForwardNavigationEvent event) {
    try {
      val loader = get(FXMLLoader.class);
      val root = (Parent) loader.load(getClass().getResourceAsStream(event.getFxmlLocation()));
      val stage = new Stage();
      stage.setScene(new Scene(root));
      val windowTitle = get(LanguageHandler.class).getCurrentLocaleBundle()
          .getString(event.getWindowTitleKey());
      stage.setTitle(windowTitle);
      stage.show();
    } catch (IOException e) {
      log.error("Failed to open a new window from " + event.getFxmlLocation(), e);
    }
  }

  /**
   * Registers a ViewModel to receive events
   *
   * @param event the RegisterViewModelEvent that contains the ViewModel to register
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  final public void registerViewModel(RegisterViewModelEvent event) {
    registeredViewModels.add(new WeakReference<>(event.getViewModel()));
  }

  /**
   * When an event is fired, forward it on to all ViewModelBases registered
   * <p>
   * This is a workaround for the lack of registerWeak in EventBus
   *
   * @param event the event fired
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  final public void onEvent(Event event) {
    for (var i = registeredViewModels.listIterator(); i.hasNext(); ) {
      val viewModel = i.next().get();
      // if the object was garbage collected, remove it from the list; otherwise, forward event
      if (viewModel == null) {
        i.remove();
      } else {
        viewModel.onEventReceived(event);
      }
    }
  }

  /**
   * Module used for accessing the database
   */
  private static class DatabaseModule extends AbstractModule {

    /**
     * Provide a single connection for database usage
     */
    @Provides
    @Singleton
    public Connection provideConnection() throws SQLException {
      log.debug("Creating an embedded database connection");
      val url = DatabaseUtilities.getURL("Odb", false);
      return DriverManager.getConnection(url);
    }
  }

  /**
   * Provides the EventBus to use (the default one)
   */
  private static class EventBusModule extends AbstractModule {

    /**
     * Creates the binding for references of EventBus to use the default
     */
    @Override
    protected void configure() {
      bind(EventBus.class).toInstance(EventBus.getDefault());
    }
  }

  /**
   * Provides FXMLLoaders to use
   */
  private static class FXMLLoaderModule extends AbstractModule {

    /**
     * Provides FXMLLoaders that are created with the injector
     *
     * @param injector the injector used to create controllers in the FXMLLoader
     * @return a new FXMLLoader that has its controller factor set to use the proper injector
     */
    @Provides
    public FXMLLoader provideLoader(Injector injector) {
      val loader = new FXMLLoader();
      loader.setControllerFactory(injector::getInstance);
      return loader;
    }
  }
}
