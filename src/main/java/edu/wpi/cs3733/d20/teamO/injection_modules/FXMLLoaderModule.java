package edu.wpi.cs3733.d20.teamO.injection_modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import javafx.fxml.FXMLLoader;
import lombok.val;

/**
 * Provides FXMLLoaders to use
 */
public class FXMLLoaderModule extends AbstractModule {

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
    loader.setResources(injector.getInstance(LanguageHandler.class).getCurrentBundle());
    return loader;
  }
}