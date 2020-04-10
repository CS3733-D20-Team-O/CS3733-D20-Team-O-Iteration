package edu.wpi.onyx_ouroboros.model.data;

/**
 * Represents what a CSVHandler must be able to do
 */
public interface ICSVHandler {

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
