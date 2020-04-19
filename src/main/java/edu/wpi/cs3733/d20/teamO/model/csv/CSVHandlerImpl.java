package edu.wpi.cs3733.d20.teamO.model.csv;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Handles parsing and exporting PrototypeNode CSV files
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class CSVHandlerImpl implements CSVHandler {

  /**
   * The database to import records into and export records from
   */
  private final DatabaseWrapper database;

  @Override
  public void importNodes(String csvFileLocation) {
    File nodeCsvFile = new File(csvFileLocation);
    try (val scanner = new Scanner(nodeCsvFile).useDelimiter(",|[\\r\\n]+")) {
      // Read in first line so that attribute names are not imported as a node
      scanner.nextLine();
      // Read each node in the file and put its data into the database
      while (scanner.hasNext()) {
        database.addNode(scanner.next(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
            scanner.next(), scanner.next(), scanner.next(), scanner.next());
      }
    } catch (FileNotFoundException e) {
      log.error("Could not read the file " + csvFileLocation, e);
    }
  }

  @Override
  public void importEdges(String csvFileLocation) {
    // todo
  }

  @Override
  public void importEmployees(String csvFileLocation) {
    // todo
  }

  @Override
  public void importServiceRequests(String csvFileLocation) {
    // todo
  }

  @Override
  public void exportNodes(String csvFileLocation) {
    val nodeMap = database.exportNodes();
    val nodeList = nodeMap.keySet().stream().map(nodeMap::get).collect(Collectors.toList());
    exportGeneric(csvFileLocation, nodeList, Node.class, "neighbors");
  }

  @Override
  public void exportEdges(String csvFileLocation) {
    exportGeneric(csvFileLocation, database.exportEdges(), Edge.class);
  }

  @Override
  public void exportEmployees(String csvFileLocation) {
    exportGeneric(csvFileLocation, database.exportEmployees(), Employee.class);
  }

  @Override
  public void exportServiceRequests(String csvFileLocation) {
    exportGeneric(csvFileLocation, database.exportServiceRequests(), ServiceRequest.class);
  }

  private <T> void exportGeneric(String csvFileLocation, List<T> entries,
      Class<T> tClass, String... excludedFields) {
    // Create the list of lines to write
    val toWrite = new LinkedList<String>();

    // Filter all declared fields down to just what we should process
    val fields = Arrays.stream(tClass.getDeclaredFields())
        .filter(field -> !Arrays.asList(excludedFields).contains(field.getName()))
        .collect(Collectors.toList());

    // Add the first (header) line
    toWrite.add(fields.stream().map(Field::getName).collect(Collectors.joining(",")));

    // Add a new line to write for each entry
    for (val entry : entries) {
      val entryAsString = fields.stream().map(field -> {
        try {
          field.setAccessible(true);
          return field.get(entry).toString();
        } catch (IllegalAccessException e) {
          log.error("Failed to convert " + field.getName() + " to a string", e);
          return "INVALID";
        }
      }).collect(Collectors.joining(","));
      toWrite.add(entryAsString);
    }

    // Print all the lines to the file
    try {
      Files.write(Paths.get(csvFileLocation), toWrite);
    } catch (IOException e) {
      log.error("Failed to write to " + csvFileLocation, e);
    }
  }
}