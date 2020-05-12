package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

/**
 * An event for when the CSS file is switched
 */
@Value
public class CSSSwitchEvent implements Event {

  String path;
}
