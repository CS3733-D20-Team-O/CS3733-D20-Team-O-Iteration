package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

/**
 * An event for when the zoom scale is changed
 */
@Value
public class ZoomEvent implements Event {

  double scale;
}
