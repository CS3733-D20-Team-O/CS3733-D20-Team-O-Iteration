package edu.wpi.cs3733.d20.teamO.model.data.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the employee table
 */
@RequiredArgsConstructor
public enum EmployeeProperty implements TableProperty {
  EMPLOYEE_ID("employeeID"),
  NAME("name"),
  TYPE("type"),
  PASSWORD("password"),
  IS_AVAILABLE("isAvailable");

  @Getter
  private final String columnName;
}
