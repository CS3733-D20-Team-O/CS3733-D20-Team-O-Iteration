package edu.wpi.onyx_ouroboros.model.data.csv;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import lombok.RequiredArgsConstructor;

/**
 * Handles parsing and exporting PrototypeNode CSV files
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class CSVHandlerImpl implements CSVHandler {

  /**
   * The database to import records into and export records from
   */
  private final DatabaseWrapper database;

  /**
   * Imports the given CSV file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  @Override
  public void importToDatabase(String csvFileLocation) {
    // todo
  }

  /**
   * Exports the database into the given CSV file
   *
   * @param csvFileLocation the path of the csv file to create/overwrite
   */
  @Override
  public void exportFromDatabase(String csvFileLocation) {
    // todo
  }
}