package edu.wpi.onyx_ouroboros.model.data.csv;

import com.google.inject.AbstractModule;

/**
 * Guice Module for CSV handling
 */
public class CSVModule extends AbstractModule {

  /**
   * Makes necessary bindings in order to create a CSVHandler
   */
  @Override
  protected void configure() {
    bind(CSVHandler.class).to(CSVHandlerImpl.class);
  }
}
