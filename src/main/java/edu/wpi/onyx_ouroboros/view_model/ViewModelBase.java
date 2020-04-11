package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageSwitchEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Labeled;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * The base view model for all other ViewModels to extend
 * <p>
 * Automatically handles language switches for appropriate fields
 */
@Slf4j
public abstract class ViewModelBase implements Initializable {

  /**
   * Called to initialize a controller after its root element has been completely processed.
   * <p>
   * ALL OVERRIDES OF ViewModelBase's initialize MUST CALL SUPER
   *
   * @param location  The location used to resolve relative paths for the root object, or null
   * @param resources The resources used to localize the root object, or null
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    EventBus.getDefault().register(this);
  }

  /**
   * Gets called when a language switch occurs (or when first initialized)
   *
   * @param langSwitchEvent the LanguageSwitchEvent that contains the new language information
   */
  @Subscribe(sticky = true)
  @SuppressWarnings("unused") // Called by EventBus
  public void onLanguageSwitch(LanguageSwitchEvent langSwitchEvent) {
    val bundle = langSwitchEvent.getBundle();
    Platform.runLater(() -> switchToNewLocale(bundle));
  }

  /**
   * Called to switch all appropriate fields of this class to the new language
   *
   * @param bundle the resource bundle to load the strings from
   */
  void switchToNewLocale(ResourceBundle bundle) {
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
