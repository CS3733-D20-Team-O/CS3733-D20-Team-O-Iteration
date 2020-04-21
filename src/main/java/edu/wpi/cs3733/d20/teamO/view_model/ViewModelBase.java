package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
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

  @Inject
  private EventBus eventBus;

  @Inject
  private LanguageHandler languageHandler;

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * @param location  The location used to resolve relative paths for the root object, or null
   * @param resources The resources used to localize the root object, or null
   */
  @Override
  public final void initialize(URL location, ResourceBundle resources) {
    dispatch(new RegisterViewModelEvent(this));
    switchToNewLocale(languageHandler.getCurrentBundle());
    start(location, resources);
  }

  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  protected void start(URL location, ResourceBundle resources) {
  }

  /**
   * Called on the JavaFX thread when any event is fired
   *
   * @param event the event was fired
   */
  protected void onEvent(Event event) {
  }

  /**
   * Gets the translated string in the current language for a given key
   *
   * @param key the key in the Strings resource bundle
   * @return the localized string from the key
   */
  final protected String getString(String key) {
    return languageHandler.getCurrentBundle().getString(key);
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
  final public void onEventReceived(Event event) {
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
