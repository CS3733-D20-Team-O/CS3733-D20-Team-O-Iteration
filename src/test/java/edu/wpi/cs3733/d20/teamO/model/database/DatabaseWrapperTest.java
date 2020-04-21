package edu.wpi.cs3733.d20.teamO.model.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.HashMap;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  private final DatabaseWrapper database = TestInjector.create(DatabaseWrapper.class);

  private final Node[] testNodes = {
      new Node("0101", 0, 0, 1,
          "Faulkner", "ELEV", "Elevator 1", "Elev1"),
      new Node("0102", 9, 0, 1,
          "Faulkner", "ELEV", "Elevator 2", "Elev2"),
      new Node("0103", 0, 5, 1,
          "Faulkner", "ELEV", "Elevator 3", "Elev3"),
  };

  @BeforeEach
  public void emptyDatabase() {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_TYPE, "ELEV");
  }

  //
//  private void checkResult(Node... expected) {
//    assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(database.export()));
//  }
//
  @Test
  public void addSameNodeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    HashMap map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addMultipleNodesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    HashMap map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    map.put("0102", testNodes[1]);
    map.put("0103", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addAndDeleteNodesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, "0102");
    HashMap map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    map.put("0103", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void failToDeleteNodesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, "123");
    HashMap map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    map.put("0102", testNodes[1]);
    map.put("0103", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addManyAndUpdateNodeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    val updatedNode = new Node("0101", 3, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, "0101", NodeProperty.X_COORD, 3);
    HashMap map = new HashMap<String, Node>();
    map.put("0101", updatedNode);
    map.put("0102", testNodes[1]);
    map.put("0103", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void failUpdateNodeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    val updatedNode = new Node("0101", 3, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, "876", NodeProperty.X_COORD, 3);
    HashMap map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    assertEquals(map, database.exportNodes());
  }

//  @Test
//  public void exportEmptyTest() {
//    checkResult(); // tests empty list with database.export()
//  }

}
