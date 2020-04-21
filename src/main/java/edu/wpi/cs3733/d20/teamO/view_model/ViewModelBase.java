package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.events.Event;
import edu.wpi.cs3733.d20.teamO.events.RegisterViewModelEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;

/**
 * The base view model for all other ViewModels to extend
 * <p>
 * Automatically handles language switches for appropriate fields and injection
 */
@Slf4j
public abstract class ViewModelBase implements Initializable {

  /**
   * The EventBus used to dispatch events
   */
  @Inject
  private EventBus eventBus;

  /**
   * The resource bundle injected by JavaFX
   */
  @FXML
  private ResourceBundle resources;

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * @param location  The location used to resolve relative paths for the root object, or null
   * @param resources The resources used to localize the root object, or null
   */
  @Override
  public final void initialize(URL location, ResourceBundle resources) {
    dispatch(new RegisterViewModelEvent(this));
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
  public void onEvent(Event event) {
  }

  /**
   * Gets the translated string in the current language for a given key
   *
   * @param key the key in the Strings resource bundle
   * @return the localized string from the key
   */
  protected final String getString(String key) {
    return resources.getString(key);
  }

  /**
   * Posts the specified event to the EventBus
   *
   * @param event the event to post
   */
  protected final void dispatch(Event event) {
    eventBus.post(event);
  }
}
