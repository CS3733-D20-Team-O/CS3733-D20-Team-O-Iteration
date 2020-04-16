package edu.wpi.cs3733.d20.teamO.model.data.database.db_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the different tables in the database
 */
@RequiredArgsConstructor
public enum Table {
  NODES_TABLE("NODES_TABLE"),
  EDGES_TABLE("EDGES_TABLE");

  @Getter
  private final String tableName;
}
