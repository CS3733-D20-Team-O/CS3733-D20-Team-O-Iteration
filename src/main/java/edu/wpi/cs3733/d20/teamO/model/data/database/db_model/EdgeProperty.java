package edu.wpi.cs3733.d20.teamO.model.data.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the edges table
 */
@RequiredArgsConstructor
public enum EdgeProperty implements TableProperty {
  EDGE_ID("edgeID"),
  START_ID("startID"),
  STOP_ID("stopID");

  @Getter
  private final String columnName;
}
