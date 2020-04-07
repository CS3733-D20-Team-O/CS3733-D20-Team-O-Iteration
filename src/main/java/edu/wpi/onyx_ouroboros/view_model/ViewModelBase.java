package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageSwitchEvent;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;
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
    EventBus.getDefault().register(this); // todo unsubscribe
  }

  /**
   * Processes all fields of subclasses and updates
   * <p>
   * Gets called when a language switch occurs (or when first initialized)
   *
   * @param langSwitchEvent the LanguageSwitchEvent that contains the new language information
   */
  @Subscribe(sticky = true)
  @SuppressWarnings("unused")
  public void onLanguageSwitch(LanguageSwitchEvent langSwitchEvent) {
    val bundle = langSwitchEvent.getBundle();
    for (val field : getClass().getFields()) {
      val annotation = field.getAnnotation(Language.class);
      if (annotation != null) {
        if (bundle.containsKey(annotation.key())) {
          setNewText(field, bundle.getString(annotation.key()));
        } else {
          log.error("@Language ID " + annotation.key() +
              " is invalid in " + getClass().getCanonicalName());
        }
      }
    }
  }

  /**
   * Sets the given field's text to newText
   *
   * @param field   the field to set the text of
   * @param newText the new text
   */
  private void setNewText(Field field, String newText) {
    try {
      Labeled.class.getMethod("setText", String.class).invoke(field.get(this), newText);
    } catch (Exception e) {
      val errorMsg = "Language switch failed for " + field.getName() +
          " in " + getClass().getCanonicalName();
      log.error(errorMsg, e);
    }
  }
}
