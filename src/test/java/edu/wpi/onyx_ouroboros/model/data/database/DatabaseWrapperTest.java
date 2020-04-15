package edu.wpi.onyx_ouroboros.model.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.onyx_ouroboros.model.TestInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests DatabaseWrapper
 */
@Slf4j
public class DatabaseWrapperTest {

  private final DatabaseWrapper database = TestInjector
      .create(DatabaseWrapperImpl.class);
  private final PrototypeNode[] testNodes = {
      new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1"),
      new PrototypeNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2"),
      new PrototypeNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3")
  };

  @BeforeEach
  public void emptyDatabase() {
    log.debug("Clearing the database!");
    database.export().forEach((node) -> database.deleteNode(node.getNodeID()));
  }

  private void checkEquals(PrototypeNode[] expected, List<PrototypeNode> actual) {
    assertEquals(new HashSet<PrototypeNode>(Arrays.asList(expected)),
        new HashSet<PrototypeNode>(actual));
  }

  @Test
  public void addSameNodeTest() {
    database.addNode(testNodes[0]);
    database.addNode(testNodes[0]);
    checkEquals(new PrototypeNode[]{testNodes[0]}, database.export());
  }

  @Test
  public void addMultipleNodesTest() {
    database.addNode(testNodes[0]);
    database.addNode(testNodes[1]);
    database.addNode(testNodes[2]);
    checkEquals(testNodes, database.export());
  }

  @Test
  public void addAndDeleteNodesTest() {
    database.addNode(testNodes[0]);
    database.addNode(testNodes[1]);
    database.addNode(testNodes[2]);
    database.deleteNode(testNodes[1].getNodeID());
    checkEquals(new PrototypeNode[]{testNodes[0], testNodes[2]}, database.export());
  }

  @Test
  public void failToDeleteNodesTest() {
    database.addNode(testNodes[0]);
    database.addNode(testNodes[1]);
    database.addNode(testNodes[2]);
    database.deleteNode("0123");
    checkEquals(testNodes, database.export());
  }

  @Test
  public void addAndUpdateNodeTest() {
    database.addNode(testNodes[0]);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 0, 1, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    database.updateNode(testNodes[0].getNodeID(), updatedNode1);
    checkEquals(new PrototypeNode[]{updatedNode1}, database.export());
  }

  @Test
  public void failUpdateNodeTest() {
    database.addNode(testNodes[0]);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 0, 1, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    database.updateNode("9876", updatedNode1);
    checkEquals(new PrototypeNode[]{testNodes[0]}, database.export());
  }

  @Test
  public void addAndUpdateManyTest() {
    database.addNode(testNodes[0]);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 2, 5, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    database.updateNode(testNodes[0].getNodeID(), updatedNode1);
    checkEquals(new PrototypeNode[]{updatedNode1}, database.export());
  }

  @Test
  public void exportEmptyTest() {
    List<PrototypeNode> list = database.export();
    assertEquals(0, list.size());
  }
}
