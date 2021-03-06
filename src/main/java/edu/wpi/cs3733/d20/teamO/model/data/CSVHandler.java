package edu.wpi.cs3733.d20.teamO.model.data;

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
  boolean importNodes(String csvFileLocation);

  /**
   * Imports edges from the given csv file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  boolean importEdges(String csvFileLocation);

  /**
   * Imports employees from the given csv file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  boolean importEmployees(String csvFileLocation);

  /**
   * Exports nodes from the database to the given csv file
   *
   * @param csvFileLocation the path of the csv file to write
   */
  boolean exportNodes(String csvFileLocation);

  /**
   * Exports edges from the database to the given csv file
   *
   * @param csvFileLocation the path of the csv file to write
   */
  boolean exportEdges(String csvFileLocation);

  /**
   * Exports employees from the database to the given csv file
   *
   * @param csvFileLocation the path of the csv file to write
   */
  boolean exportEmployees(String csvFileLocation);
}
