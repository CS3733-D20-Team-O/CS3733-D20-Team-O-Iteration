package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.LanguageSwitchEvent;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import edu.wpi.cs3733.d20.teamO.model.language.Language;
import edu.wpi.cs3733.d20.teamO.model.language.LanguageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Labeled;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

/**
 * The base view model for all other ViewModels to extend
 * <p>
 * Automatically handles language switches for appropriate fields and injection
 */
@Slf4j
public abstract class ViewModelBase implements Initializable {

  /**
   * The Injector used to create objects when they are needed (such as FXML loaders)
   */
  @Inject
  private Injector injector;

  /**
   * The EventBus to dispatch events to
   */
  @Inject
  private EventBus eventBus;

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * @param location  The location used to resolve relative paths for the root object, or null
   * @param resources The resources used to localize the root object, or null
   */
  @Override
  public final void initialize(URL location, ResourceBundle resources) {
    dispatch(new RegisterViewModelEvent(this));
    switchToNewLocale(LanguageHandler.getCurrentLocaleBundle());
    start(location);
  }

  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location the location used to resolve relative paths for the root object, or null
   */
  protected void start(URL location) {
  }

  /**
   * Called on the JavaFX thread when any event is fired
   *
   * @param event the event was fired
   */
  protected void onEvent(Event event) {
  }

  /**
   * Creates objects of the specified type
   *
   * @param tClass the class of the object to create
   * @param <T>    the type of object to create
   * @return the new object of the specified type
   */
  final protected <T> T create(Class<T> tClass) {
    return injector.getInstance(tClass);
  }

  /**
   * Posts the specified event to the EventBus
   *
   * @param event the event to post
   */
  final protected void dispatch(Event event) {
    eventBus.post(event);
  }

  /**
   * Called when an event is received
   *
   * @param event the event that was received
   */
  public final void onEventReceived(Event event) {
    Platform.runLater(() -> {
      if (event instanceof LanguageSwitchEvent) {
        val bundle = ((LanguageSwitchEvent) event).getBundle();
        switchToNewLocale(bundle);
      }
      onEvent(event);
    });
  }

  /**
   * Called to switch all appropriate fields of this class to the new language
   *
   * @param bundle the resource bundle to load the strings from
   */
  final void switchToNewLocale(ResourceBundle bundle) {
    for (val field : getClass().getDeclaredFields()) {
      val annotation = field.getAnnotation(Language.class);
      // If this field is not annotated, ignore it (we only work on annotated fields)
      if (annotation != null) {
        if (bundle.containsKey(annotation.key())) {
          try {
            val newText = bundle.getString(annotation.key());
            field.setAccessible(true);
            val object = field.get(this);
            if (object instanceof Labeled) {
              ((Labeled) object).setText(newText);
            } else {
              log.warn("Language switch failed for " + field.getName() + " in " +
                  getClass().getCanonicalName() + " because it is not a Labeled!");
            }
          } catch (IllegalAccessException e) {
            log.error("IMPLEMENTATION ERROR! Could not field.get()", e);
          }
        } else {
          log.error("@Language key " + annotation.key() +
              " is invalid in " + getClass().getCanonicalName());
        }
      }
    }
  }
}
