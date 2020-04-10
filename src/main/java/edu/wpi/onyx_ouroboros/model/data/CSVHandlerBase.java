package edu.wpi.onyx_ouroboros.model.data;

import lombok.Data;

/**
 * Handles parsing and exporting PrototypeNode CSV files
 */
@Data
public abstract class CSVHandlerBase {

  /**
   * The database to import records into and export records from
   */
  private final IDatabaseWrapper database;

  /**
   * Imports the given CSV file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  public abstract void importToDatabase(String csvFileLocation);

  /**
   * Exports the database into the given CSV file
   *
   * @param csvFileLocation the path of the csv file to create/overwrite
   */
  public abstract void exportFromDatabase(String csvFileLocation);
}
