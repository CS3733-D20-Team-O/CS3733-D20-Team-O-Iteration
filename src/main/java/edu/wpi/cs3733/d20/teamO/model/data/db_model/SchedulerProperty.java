package edu.wpi.cs3733.d20.teamO.model.data.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the scheduler table
 */
@RequiredArgsConstructor
public enum SchedulerProperty implements TableProperty {
  SCHEDULER_ID("schedulerID"),
  EMPLOYEE_ID("employeeID"),
  START_TIME("startTime"),
  END_TIME("endTime"),
  ROOM_TYPE("roomType");

  @Getter
  private final String columnName;
}
