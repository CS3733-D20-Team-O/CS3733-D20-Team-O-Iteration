package edu.wpi.cs3733.d20.teamO;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseUtilities;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Slf4j
public abstract class ApplicationBase extends Application {

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

  @Override
  final public void init() {
    log.info("Starting up " + getClass().getSimpleName());
    get(EventBus.class).register(this);
    // Set English as the default language
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  @Override
  final public void stop() throws SQLException {
    log.info("Stopping " + getClass().getSimpleName());
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
  static class DatabaseModule extends AbstractModule {

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
  static class EventBusModule extends AbstractModule {

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
  static class FXMLLoaderModule extends AbstractModule {

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
      loader.setResources(injector.getInstance(LanguageHandler.class).getCurrentLocaleBundle());
      return loader;
    }
  }
}
