package edu.wpi.onyx_ouroboros.model.data.csv;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
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

  /**
   * Imports the given CSV file into the database
   *
   * @param csvFileLocation the path of the csv file to read
   */
  @Override
  public void importToDatabase(String csvFileLocation) {
    // todo use scanner delimiter instead of .split()
    File nodeCsvFile = new File(csvFileLocation);
    try (val scanner = new Scanner(nodeCsvFile)) {
      // Read in first line so that attribute names are not imported as a node
      scanner.nextLine();
      // Read each node in the file and put its data into the database
      while (scanner.hasNextLine()) {
        // Read the node attributes from the line as an array of strings
        val nodeAttributes = scanner.nextLine().split(",");
        val nodeID = nodeAttributes[0];
        val xCoord = Integer.parseInt(nodeAttributes[1]);
        val yCoord = Integer.parseInt(nodeAttributes[2]);
        val floor = Integer.parseInt(nodeAttributes[3]);
        val building = nodeAttributes[4];
        val nodeType = nodeAttributes[5];
        val longName = nodeAttributes[6];
        val shortName = nodeAttributes[7];
        database.addNode(nodeID, xCoord, yCoord, floor,
            building, nodeType, longName, shortName);
      }
    } catch (FileNotFoundException e) {
      log.error("Could not read the file " + csvFileLocation, e);
    }
  }

  /**
   * Exports the database into the given CSV file
   *
   * @param csvFileLocation the path of the csv file to create/overwrite
   */
  @Override
  public void exportFromDatabase(String csvFileLocation) {
// todo consider other options for writing files so we don't need to call System.lineSeparator()
//    val nodes = database.export();
//    try (val writer = new FileWriter(csvFileLocation)) {
//      // Write header to file
//      writer.write("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName" +
//          System.lineSeparator());
//      for (PrototypeNode node : nodes) {
//        String nodeID = node.getNodeID();
//        String xCoord = Integer.toString(node.getXCoord());
//        String yCoord = Integer.toString(node.getYCoord());
//        String floor = Integer.toString(node.getFloor());
//        String building = node.getBuilding();
//        String nodeType = node.getNodeType();
//        String longName = node.getLongName();
//        String shortName = node.getShortName();
//        String line = String.join(",", nodeID, xCoord, yCoord, floor,
//            building, nodeType, longName, shortName);
//        writer.write(line + System.lineSeparator());
//      }
//    } catch (IOException e) {
//      log.warn("Error while trying to export nodes to " + csvFileLocation, e);
//    }
  }
}