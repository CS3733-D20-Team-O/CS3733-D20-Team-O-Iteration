package edu.wpi.cs3733.d20.teamO.model.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.val;
import org.junit.jupiter.api.Test;

/**
 * Tests CSVHandler
 */
public class CSVHandlerTest {

  /**
   * Tests both importNodes and exportNodes by importing a file, exporting it, and checking to see
   * if the resulting exported file:
   * <ul>
   *   <li>is the same length as the original (no nodes dropped)</li>
   *   <li>has the same contents as the original (no nodes altered)</li>
   * </ul>
   */
  @Test
  public void testCSVHandlerForNodes() throws IOException, URISyntaxException {
    val csvHandler = TestInjector.create(CSVHandler.class);
    // todo clean this up but also keep it working on windows
    val input = Paths.get(getClass().getResource("PrototypeNodes.csv").toURI()).toString();
    val output = "build/Nodes.csv";
    csvHandler.importNodes(input);
    csvHandler.exportNodes(output);
    val inputLines = Files.readAllLines(Paths.get(input));
    val outputLines = Files.readAllLines(Paths.get(output));
    assertEquals(inputLines.size(), outputLines.size());
    assertEquals(inputLines.get(0), outputLines.get(0)); // make sure header lines are the same
    assertTrue(outputLines.containsAll(inputLines));
    assertTrue(inputLines.containsAll(outputLines));
  }

  /**
   * Tests both importEdges and exportEdges by importing a file, exporting it, and checking to see
   * if the resulting exported file:
   * <ul>
   *   <li>is the same length as the original (no edges dropped)</li>
   *   <li>has the same contents as the original (no edges altered)</li>
   * </ul>
   */
  @Test
  public void testCSVHandlerForEdges() throws IOException, URISyntaxException {
    val csvHandler = TestInjector.create(CSVHandler.class);
    // todo clean this up but also keep it working on windows
    val input = Paths.get(getClass().getResource("PrototypeEdges.csv").toURI()).toString();
    val output = "build/Edges.csv";

    //nodes must be imported first so that they can be recognized while importing edges
    val nodesPath = Paths.get(getClass().getResource("PrototypeNodes.csv").toURI()).toString();
    csvHandler.importNodes(nodesPath);

    csvHandler.importEdges(input);
    csvHandler.exportEdges(output);
    val inputLines = Files.readAllLines(Paths.get(input));
    val outputLines = Files.readAllLines(Paths.get(output));
    assertEquals(inputLines.size(), outputLines.size());
    assertEquals(inputLines.get(0), outputLines.get(0)); // make sure header lines are the same
    assertTrue(outputLines.containsAll(inputLines));
    assertTrue(inputLines.containsAll(outputLines));
  }

  /**
   * Tests both importEmployees and exportEmployees by importing a file, exporting it, and checking
   * to see if the resulting exported file:
   * <ul>
   *   <li>is the same length as the original (no employees dropped)</li>
   *   <li>has the same contents as the original (no employees altered)</li>
   * </ul>
   */
  @Test
  public void testCSVHandlerForEmployees() throws IOException, URISyntaxException {
    val csvHandler = TestInjector.create(CSVHandler.class);
    // todo clean this up but also keep it working on windows
    val input = Paths.get(getClass().getResource("PrototypeEmployees.csv").toURI()).toString();
    val output = "build/Employees.csv";
    csvHandler.importEmployees(input);
    csvHandler.exportEmployees(output);
    val inputLines = Files.readAllLines(Paths.get(input));
    val outputLines = Files.readAllLines(Paths.get(output));
    assertEquals(inputLines.size(), outputLines.size());
    assertEquals(inputLines.get(0), outputLines.get(0)); // make sure header lines are the same
    assertTrue(outputLines.containsAll(inputLines));
    assertTrue(inputLines.containsAll(outputLines));
  }

  /*
  /**
   * Tests both importServiceRequests and exportServiceRequests by importing a file, exporting it,
   * and checking to see if the resulting exported file:
   * <ul>
   *   <li>is the same length as the original (no serviceRequests dropped)</li>
   *   <li>has the same contents as the original (no serviceRequests altered)</li>
   * </ul>
   *
  @Test
  public void testCSVHandlerForServiceRequests() throws IOException, URISyntaxException {
    val csvHandler = TestInjector.create(CSVHandler.class);
    // todo clean this up but also keep it working on windows
    val input = Paths.get(getClass().getResource("PrototypeServiceRequests.csv").toURI())
  .toString();
  val output = "build/ServiceRequests.csv";

  //nodes and employees must be imported first so that they can be recognized while importing service requests
  val nodesPath = Paths.get(getClass().getResource("PrototypeNodes.csv").toURI()).toString();
  csvHandler.importNodes(nodesPath);
  val employeesPath = Paths.get(getClass().getResource("PrototypeEmployees.csv").toURI())
  .toString();
  csvHandler.importEmployees(employeesPath);

  csvHandler.importServiceRequests(input);
  csvHandler.exportServiceRequests(output);
  val inputLines = Files.readAllLines(Paths.get(input));
  val outputLines = Files.readAllLines(Paths.get(output));
  assertEquals(inputLines.size(), outputLines.size());
  assertEquals(inputLines.get(0), outputLines.get(0)); // make sure header lines are the same
  assertTrue(outputLines.containsAll(inputLines));
  assertTrue(inputLines.containsAll(outputLines));
  }*/
}
