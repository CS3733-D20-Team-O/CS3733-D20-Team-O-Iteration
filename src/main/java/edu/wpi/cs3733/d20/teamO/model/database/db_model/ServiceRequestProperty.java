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
  SUBMIT_TIME("submitTime"),
  COMPLETE_TIME("completeTime"),
  NODE_ID("nodeID"),
  TYPE("type"),
  WHO_MARKED("whoMarked");

  @Getter
  private final String columnName;
}
