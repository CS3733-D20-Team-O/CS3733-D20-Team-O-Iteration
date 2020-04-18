package edu.wpi.cs3733.d20.teamO.model.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the service request table
 */
@RequiredArgsConstructor
public enum ServiceRequestProperty implements TableProperty {
  REQUEST_ID("requestID"),
  IS_COMPLETE("isComplete"),
  COMPLETE_TIME("completeTime"),
  SUBMIT_TIME("submitTime"),
  NODE_ID("nodeID"),
  TYPE("type"),
  WHO_MARKED("whoMarked");

// todo other columns here

  @Getter
  private final String columnName;
}
