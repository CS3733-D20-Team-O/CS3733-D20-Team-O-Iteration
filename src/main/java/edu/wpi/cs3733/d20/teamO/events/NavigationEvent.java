package edu.wpi.cs3733.d20.teamO.events;

import lombok.Value;

@Value
public class NavigationEvent implements Event {

  String windowTitleKey, fxmlLocation;
}
