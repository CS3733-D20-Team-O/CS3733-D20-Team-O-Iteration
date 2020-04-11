package edu.wpi.onyx_ouroboros.model;

import com.google.inject.AbstractModule;
import org.greenrobot.eventbus.EventBus;

/**
 * Sets the default EventBus to use in objects created via Guice
 */
public class EventBusModule extends AbstractModule {

  /**
   * Makes necessary binding for objects to be able to access an EventBus
   */
  @Override
  protected void configure() {
    bind(EventBus.class).toInstance(EventBus.getDefault());
  }
}
