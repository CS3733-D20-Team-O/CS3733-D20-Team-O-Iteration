package edu.wpi.cs3733.d20.teamO.model.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a column in the nodes table
 */
@RequiredArgsConstructor
public enum NodeProperty implements TableProperty {
  NODE_ID("nodeID"),
  X_COORD("xCoord"),
  Y_COORD("yCoord"),
  FLOOR("floor"),
  BUILDING("building"),
  NODE_TYPE("nodeType"),
  LONG_NAME("longName"),
  SHORT_NAME("shortName");

  @Getter
  private final String columnName;

  @Override
  public Table getTable() {
    return Table.NODES_TABLE;
  }
}
