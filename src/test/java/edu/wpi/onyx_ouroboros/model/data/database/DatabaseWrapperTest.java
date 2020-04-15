package edu.wpi.onyx_ouroboros.model.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.onyx_ouroboros.model.TestInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.util.Arrays;
import java.util.HashSet;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  private final DatabaseWrapper database = TestInjector.create(DatabaseWrapperImpl.class);

  private final PrototypeNode[] testNodes = {
      new PrototypeNode("0101", 0, 0, 1,
          "Faulkner", "ELEV", "Elevator 1", "Elev1"),
      new PrototypeNode("0102", 9, 0, 1,
          "Faulkner", "ELEV", "Elevator 2", "Elev2"),
      new PrototypeNode("0103", 0, 5, 1,
          "Faulkner", "ELEV", "Elevator 3", "Elev3"),
  };

  @BeforeEach
  public void emptyDatabase() {
    database.export().forEach((node) -> database.deleteNode(node.getNodeID()));
  }

  private void checkResult(PrototypeNode... expected) {
    assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(database.export()));
  }

  @Test
  public void addSameNodeTest() {
    database.addNode(testNodes[0]);
    database.addNode(testNodes[0]);
    checkResult(testNodes[0]);
  }

  @Test
  public void addMultipleNodesTest() {
    Arrays.asList(testNodes).forEach(database::addNode);
    checkResult(testNodes);
  }

  @Test
  public void addAndDeleteNodesTest() {
    Arrays.asList(testNodes).forEach(database::addNode);
    database.deleteNode(testNodes[1].getNodeID());
    checkResult(testNodes[0], testNodes[2]);
  }

  @Test
  public void failToDeleteNodesTest() {
    Arrays.asList(testNodes).forEach(database::addNode);
    database.deleteNode("0123");
    checkResult(testNodes);
  }

  @Test
  public void addManyAndUpdateNodeTest() {
    Arrays.asList(testNodes).forEach(database::addNode);
    val updatedNode = new PrototypeNode("0101", 1, 0, 1,
        "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.updateNode(testNodes[0].getNodeID(), updatedNode);
    checkResult(updatedNode, testNodes[1], testNodes[2]);
  }

  @Test
  public void failUpdateNodeTest() {
    database.addNode(testNodes[0]);
    val updatedNode = new PrototypeNode("0101", 1, 0, 1,
        "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.updateNode("9876", updatedNode);
    checkResult(testNodes[0]);
  }

  @Test
  public void exportEmptyTest() {
    checkResult(); // tests empty list with database.export()
  }
}
