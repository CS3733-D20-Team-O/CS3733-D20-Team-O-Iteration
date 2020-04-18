package edu.wpi.cs3733.d20.teamO.injection_modules;

import com.google.inject.AbstractModule;
import org.greenrobot.eventbus.EventBus;

/**
 * Provides the EventBus to use (the default one)
 */
public class EventBusModule extends AbstractModule {

  /**
   * Creates the binding for references of EventBus to use the default
   */
  @Override
  protected void configure() {
    bind(EventBus.class).toInstance(EventBus.getDefault());
  }
}