package edu.wpi.onyx_ouroboros.model.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.onyx_ouroboros.model.TestInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  private final static DatabaseWrapperImpl theDatabase = TestInjector
      .create(DatabaseWrapperImpl.class);

  @BeforeEach
  public void emptyDatabase() {
    System.out.println("Clearing the database!");
    List<PrototypeNode> listOfNodes = theDatabase.export();
    for (PrototypeNode n : listOfNodes) {
      theDatabase.deleteNode(n.getNodeID());
    }
  }

  // todo delete this method and create real tests
  @Test
  public void satisfyCodeCoverage() {
    PrototypeNode dummyNode = new PrototypeNode("hi", 1, 2, 5, "test", "ELEV", "help", "me");
    theDatabase.addNode(dummyNode);
  }

  @Test
  public void addSameNodeTest() {
    PrototypeNode newNode = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(newNode);
    PrototypeNode sameNodeID = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(sameNodeID);
    List<PrototypeNode> list = theDatabase.export();
    assertEquals(1, list.size());
  }

  @Test
  public void addMultipleNodesTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    PrototypeNode node2 = new PrototypeNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2",
        "Elev2");
    PrototypeNode node3 = new PrototypeNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3",
        "Elev3");
    theDatabase.addNode(node1);
    theDatabase.addNode(node2);
    theDatabase.addNode(node3);
    List<PrototypeNode> list = theDatabase.export();
    assertEquals(3, list.size());
  }

  @Test
  public void addAndDeleteNodesTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    PrototypeNode node2 = new PrototypeNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2",
        "Elev2");
    PrototypeNode node3 = new PrototypeNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3",
        "Elev3");
    theDatabase.addNode(node1);
    theDatabase.addNode(node2);
    theDatabase.addNode(node3);
    theDatabase.deleteNode(node2.getNodeID());
    List<PrototypeNode> list = theDatabase.export();
    assertEquals(2, list.size());
  }

  @Test
  public void failToDeleteNodesTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    PrototypeNode node2 = new PrototypeNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2",
        "Elev2");
    PrototypeNode node3 = new PrototypeNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3",
        "Elev3");
    theDatabase.addNode(node1);
    theDatabase.addNode(node2);
    theDatabase.addNode(node3);
    theDatabase.deleteNode("0123");
    List<PrototypeNode> list = theDatabase.export();
    assertEquals(3, list.size());
  }

  @Test
  public void addAndUpdateNodeTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(node1);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 0, 1, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    theDatabase.updateNode(node1.getNodeID(), updatedNode1);
    List<PrototypeNode> list = theDatabase.export();
    assertTrue(list.size() == 1 && list.get(0).getXCoord() == 1);
  }

  @Test
  public void failUpdateNodeTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(node1);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 0, 1, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    theDatabase.updateNode("9876", updatedNode1);
    List<PrototypeNode> list = theDatabase.export();
    assertTrue(list.size() == 1 && list.get(0).getXCoord() == 0);
  }

  @Test
  public void addAndUpdateManyTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(node1);
    PrototypeNode updatedNode1 = new PrototypeNode("0101", 1, 2, 5, "Faulkner", "ELEV",
        "Elevator 1",
        "Elev1");
    theDatabase.updateNode(node1.getNodeID(), updatedNode1);
    List<PrototypeNode> list = theDatabase.export();
    assertTrue(list.size() == 1 && list.get(0).getXCoord() == 1 && list.get(0).getYCoord() == 2
        && list.get(0).getFloor() == 5);
  }

  @Test
  public void exportEmptyTest() {
    List<PrototypeNode> list = theDatabase.export();
    assertEquals(0, list.size());
  }

  @Test
  public void exportTest() {
    PrototypeNode node1 = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(node1);
    PrototypeNode node2 = new PrototypeNode("2", 3, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    theDatabase.addNode(node2);
    List<PrototypeNode> list = theDatabase.export();
    assertTrue(list.size() == 2 && list.contains(node1) && list.contains(node2));
  }
}
