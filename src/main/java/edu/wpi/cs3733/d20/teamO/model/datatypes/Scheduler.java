package edu.wpi.cs3733.d20.teamO.model.datatypes;

import lombok.Value;

/**
 * Represents a room schedule request in the database
 */
@Value
public class Scheduler {

  String schedulerID, employeeID, startTime, endTime, roomType;

}
