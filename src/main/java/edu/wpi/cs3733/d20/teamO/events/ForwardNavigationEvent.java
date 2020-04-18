package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

/**
 * Represents a "forward" navigation event (going to a new screen)
 */
@Value
public class ForwardNavigationEvent implements Event {

  String titleKey, fxmlLocation;
}
