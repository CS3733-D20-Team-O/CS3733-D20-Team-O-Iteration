package edu.wpi.onyx_ouroboros.model.data.csv;

/**
 * Represents what a CSVHandler must be able to do
 */
public interface CSVHandler {

  /**
   * @return a new CSVHandler
   */
  static CSVHandler getInstance() {
    // todo use dependency injection
    return new CSVHandlerImpl(null);
  }

  /**
   * Imports the given CSV file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  void importToDatabase(String csvFileLocation);

  /**
   * Exports the database into the given CSV file
   *
   * @param csvFileLocation the path of the csv file to create/overwrite
   */
  void exportFromDatabase(String csvFileLocation);
}
