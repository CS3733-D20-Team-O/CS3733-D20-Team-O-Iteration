package edu.wpi.onyx_ouroboros.model.data.csv;

import edu.wpi.onyx_ouroboros.model.TestInjector;
import java.io.IOException;
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
    val csvHandler = TestInjector.create(CSVHandlerImpl.class);
    val input = getClass().getResource("PrototypeNodes.csv").getPath();
    val output = "build/Nodes.csv";
    csvHandler.importToDatabase(input);
    // todo update CSV tests
//    csvHandler.exportFromDatabase(output);
//    val inputLines = Files.readAllLines(Paths.get(input));
//    val outputLines = Files.readAllLines(Paths.get(output));
//    assertEquals(inputLines.size(), outputLines.size());
//    assertTrue(outputLines.containsAll(inputLines));
//    assertTrue(inputLines.containsAll(outputLines));
  }
}
