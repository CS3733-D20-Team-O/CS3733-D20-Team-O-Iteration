package edu.wpi.cs3733.d20.teamO.model.datatypes;

import lombok.Value;

/**
 * Represents an employee in the database
 */
@Value
public class Employee {

  String employeeID, name, type;
  boolean isAvailable;
}
