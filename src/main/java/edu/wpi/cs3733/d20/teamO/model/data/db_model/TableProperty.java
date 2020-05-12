package edu.wpi.cs3733.d20.teamO.model.data.db_model;

/**
 * Specifies what a property of a specific table must be able to do
 */
public interface TableProperty {

  /**
   * @return the column name of the specified property
   */
  String getColumnName();
}
