package edu.wpi.cs3733.d20.teamO.model.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the nodes table
 */
@RequiredArgsConstructor
public enum ServiceRequestProperty implements TableProperty {
  REQUEST_ID("nodeID");
// todo other columns here

  @Getter
  private final String columnName;
}
