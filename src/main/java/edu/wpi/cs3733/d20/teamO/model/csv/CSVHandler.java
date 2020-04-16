package edu.wpi.cs3733.d20.teamO.model.csv;

import com.google.inject.ImplementedBy;

/**
 * Represents what a CSVHandler must be able to do
 */
@SuppressWarnings("all") // Suppresses the CSVHandlerImpl is uninstantiable warning
@ImplementedBy(value = CSVHandlerImpl.class)
public interface CSVHandler {

  /**
   * Imports nodes from the given csv file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  void importNodes(String csvFileLocation);

  /**
   * Imports edges from the given csv file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  void importEdges(String csvFileLocation);

  /**
   * Exports nodes from the database to the given csv file
   *
   * @param csvFileLocation the path of the csv file to write
   */
  void exportNodes(String csvFileLocation);

  /**
   * Exports edges from the database to the given csv file
   *
   * @param csvFileLocation the path of the csv file to write
   */
  void exportEdges(String csvFileLocation);
}
