package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

/**
 * An event for when the building is switched
 */
@Value
public class BuildingSwitchEvent implements Event {

  String building;
}
