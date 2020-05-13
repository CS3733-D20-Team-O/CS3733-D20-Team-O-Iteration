package edu.wpi.cs3733.d20.teamO.model.data.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the service request table
 */
@RequiredArgsConstructor
public enum ServiceRequestProperty implements TableProperty {
  REQUEST_ID("requestID"),
  REQUEST_TIME("requestTime"),
  REQUEST_NODE("requestNode"),
  TYPE("type"),
  STATUS("status"),
  REQUESTER_NAME("requesterName"),
  WHO_MARKED("whoMarked"),
  EMPLOYEE_ASSIGNED("employeeAssigned"),
  DATA("data");

  @Getter
  private final String columnName;
}
