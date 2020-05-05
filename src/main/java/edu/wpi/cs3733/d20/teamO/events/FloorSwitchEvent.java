package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

/**
 * An event for when the floor is switched
 */
@Value
public class FloorSwitchEvent implements Event {
  String floor;
}
