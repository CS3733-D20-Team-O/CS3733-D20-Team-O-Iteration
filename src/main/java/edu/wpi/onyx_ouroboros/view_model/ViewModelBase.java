package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.model.language.LanguageModel;
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
 *
 * <p>Automatically handles language switches for appropriate fields
 */
@Slf4j
public abstract class ViewModelBase implements Initializable {

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * <p>ALL OVERRIDES OF ViewModelBase's initialize MUST CALL SUPER
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
   *
   * <p>Gets called when a language switch occurs (or when first initialized)
   *
   * @param langModel the new language model to fuel labeled elements
   */
  @Subscribe
  @SuppressWarnings("unused")
  public void onLanguageSwitch(LanguageModel langModel) {
    for (val field : getClass().getFields()) {
      val annotation = field.getAnnotation(Language.class);
      if (annotation != null) {
        try {
          val newString =
              (String) langModel.getClass().getMethod(annotation.ID()).invoke(langModel);
          Labeled.class.getMethod("setText", String.class).invoke(field, newString);
        } catch (Exception e) {
          log.error("Language switch failed for a field in " + getClass().getCanonicalName(), e);
        }
      }
    }
  }
}
