package edu.wpi.cs3733.d20.teamO.model.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the employee table
 */
@RequiredArgsConstructor
public enum EmployeeProperty implements TableProperty {
  EMPLOYEE_ID("employeeID"),
  NAME("name"),
  IS_AVAILABLE("isAvailable"),
  TYPE("type");

  @Getter
  private final String columnName;
}
