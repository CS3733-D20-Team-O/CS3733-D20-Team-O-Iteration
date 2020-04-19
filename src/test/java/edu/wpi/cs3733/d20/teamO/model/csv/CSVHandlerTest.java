package edu.wpi.cs3733.d20.teamO.model.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.val;
import org.junit.jupiter.api.Test;

/**
 * Tests CSVHandler
 */
public class CSVHandlerTest {

  /**
   * Tests both importToDatabase and exportFromDatabase by importing a file, exporting it, and
   * checking to see if the resulting exported file:
   * <ul>
   *   <li>is the same length as the original (no nodes dropped)</li>
   *   <li>has the same contents as the original (no nodes altered)</li>
   * </ul>
   */
  @Test
  public void testCSVHandler() throws IOException {
    val csvHandler = TestInjector.create(CSVHandler.class);
    val input = getClass().getResource("PrototypeNodes.csv").getPath();
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
}
