package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.wpi.cs3733.d20.teamO.events.BackwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.ForwardNavigationEvent;
import edu.wpi.cs3733.d20.teamO.events.LanguageSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import edu.wpi.cs3733.d20.teamO.injection_modules.DatabaseModule;
import edu.wpi.cs3733.d20.teamO.injection_modules.EventBusModule;
import edu.wpi.cs3733.d20.teamO.injection_modules.FXMLLoaderModule;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseUtilities;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.view_model.NavigationBar;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import edu.wpi.cs3733.d20.teamO.view_model.main_screen.AdminViewModel;
import edu.wpi.cs3733.d20.teamO.view_model.main_screen.KioskViewModel;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
   * The root of the entire application
   */
  private final StackPane root = new StackPane();

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
    get(EventBus.class).register(this);
    // Set English as the default language (and trigger the loading of the main screen)
    get(LanguageHandler.class).setCurrentLocale(LanguageHandler.SUPPORTED_LOCALES[0]);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
  public void stop() throws SQLException {
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
  private <T> T get(Class<T> tClass) {
    return injector.getInstance(tClass);
  }

      /*
    todo incorporate this animation code too
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(imgView2.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t->{
            // remove pane and restore scene 1
            root1.getChildren().setAll(rectangle1);
        });
     */

  /**
   * Navigates to a new window within the application
   *
   * @param event the navigation event
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onForwardNavigationEvent(ForwardNavigationEvent event) {
    try {
      // Create the new root for the page
      val newRoot = new AnchorPane();

      // Set up the navigation bar for the new page
      val navBarLoader = get(FXMLLoader.class);
      val navBarRoot = (Region) navBarLoader.load(getClass()
          .getResourceAsStream("views/NavigationBar.fxml"));
      ((NavigationBar) navBarLoader.getController()).setTitle(event.getTitle());
      AnchorPane.setTopAnchor(navBarRoot, 0.0);
      AnchorPane.setLeftAnchor(navBarRoot, 0.0);
      AnchorPane.setRightAnchor(navBarRoot, 0.0);
      newRoot.getChildren().add(navBarRoot);

      // Set up the actual body (content) of the new page
      val contentLoader = get(FXMLLoader.class);
      val contentRoot = (Region) contentLoader.load(getClass()
          .getResourceAsStream(event.getFxmlLocation()));
      // The height is not available right away, so set top bounds when it is available
      navBarRoot.heightProperty().addListener(((observable, oldValue, newValue) ->
          AnchorPane.setTopAnchor(contentRoot, newValue.doubleValue())));
      AnchorPane.setLeftAnchor(contentRoot, 0.0);
      AnchorPane.setRightAnchor(contentRoot, 0.0);
      AnchorPane.setBottomAnchor(contentRoot, 0.0);
      newRoot.getChildren().add(contentRoot);

      // At this point, newRoot is completely instantiated so execute the transition
      val currentRoot = root.getChildren().get(0);
      root.getChildren().add(newRoot);
      val width = root.getWidth();
      val start = new KeyFrame(Duration.ZERO,
          new KeyValue(currentRoot.translateXProperty(), 0),
          new KeyValue(newRoot.translateXProperty(), width));
      val end = new KeyFrame(Duration.seconds(0.15),
          new KeyValue(currentRoot.translateXProperty(), -width),
          new KeyValue(newRoot.translateXProperty(), 0));
      val slide = new Timeline(start, end);
      slide.setOnFinished(e -> root.getChildren().remove(currentRoot));
      slide.play();
    } catch (IOException e) {
      log.error("Failed to switch to a new window from " + event.getFxmlLocation(), e);
    }
  }

  /**
   * Navigates back to the main screen
   *
   * @param event the event that was fired
   * @throws IOException in case the main screen cannot be loaded
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onBackwardNavigationEvent(BackwardNavigationEvent event) throws IOException {
    // Get a copy of the current root (it will be the only child of the actual root)
    val currentRoot = root.getChildren().get(0);
    // Add the main screen back to the root to get ready for the slide transition
    loadMainScreen();
    val mainScreenRoot = root.getChildren().get(1);
    // Start the slide animation
    val width = root.getWidth();
    val start = new KeyFrame(Duration.ZERO,
        new KeyValue(mainScreenRoot.translateXProperty(), -width),
        new KeyValue(currentRoot.translateXProperty(), 0));
    val end = new KeyFrame(Duration.seconds(0.15),
        new KeyValue(mainScreenRoot.translateXProperty(), 0),
        new KeyValue(currentRoot.translateXProperty(), width));
    val slide = new Timeline(start, end);
    slide.setOnFinished(e -> root.getChildren().remove(currentRoot));
    slide.play();
  }

  /**
   * Loads the main application screen into the root
   * <p>
   * TODO Cleanup this method and make more efficient (cache login details in init probably)
   *
   * @throws IOException in case the main screen cannot be loaded
   */
  private void loadMainScreen() throws IOException {
    // Setup the loader for the correct mode
    val loader = get(FXMLLoader.class);
    if (new LoginDetails(getParameters().getRaw()).isValid()) {
      loader.setController(get(AdminViewModel.class));
    } else {
      loader.setController(get(KioskViewModel.class));
    }
    // Load and add the main screen to the root
    root.getChildren().add(loader.load(getClass().getResourceAsStream("views/Main.fxml")));
  }

  /**
   * When a language event is fired, reload the main screen to be in the new language
   *
   * @param event the event that was fired
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onLanguageSwitch(LanguageSwitchEvent event) {
    root.getChildren().clear();
    try {
      loadMainScreen();
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
        viewModel.onEventReceived(event);
      }
    }
  }
}
