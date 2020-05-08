package edu.wpi.cs3733.d20.teamO.model.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.SchedulerProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Scheduler;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  private DatabaseWrapper database;

  @BeforeEach
  public void initializeDB() {
    database = TestInjector.create(DatabaseWrapper.class);
    database.addNode("OELEV00101", 0, 0, "1", "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("OELEV00201", 9, 0, "1", "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("OELEV00301", 0, 5, "1", "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEdge("01", "OELEV00101", "OELEV00201");
    database.addEdge("02", "OELEV00101", "OELEV00301");
    database.addEdge("03", "OELEV00201", "OELEV00301");
    database.addEmployee("01", "Jeff", "Gift", "password", false);
    database.addEmployee("02", "Jeff", "Gift", "password", false);
    database.addEmployee("03", "Liz", "Interpreter", "password", true);
  }

  private final Node[] testNodes = {
      new Node("OELEV00101", 0, 0, "1",
          "Faulkner", "ELEV", "Elevator 1", "Elev1"),
      new Node("OELEV00201", 9, 0, "1",
          "Faulkner", "ELEV", "Elevator 2", "Elev2"),
      new Node("OELEV00301", 0, 5, "1",
          "Faulkner", "ELEV", "Elevator 3", "Elev3"),
  };

  private final Edge[] testEdges = {
      new Edge("01", "OELEV00101", "OELEV00201"),
      new Edge("02", "OELEV00101", "OELEV00301"),
      new Edge("03", "OELEV00201", "OELEV00301"),
  };

  private final Employee[] testEmployees = {
      new Employee("01", "Jeff", "Gift", "password", false),
      new Employee("02", "Jeff", "Gift", "password", false),
      new Employee("03", "Liz", "Interpreter", "password", true),
      new Employee("0", "", "", "", false),
  };

  private void checkResultEdges(Edge... expected) {
    assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(database.exportEdges()));
  }

  private void checkResultEmployees(Employee... expected) {
    assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(database.exportEmployees()));
  }

  private void checkResultServiceRequests(ServiceRequest... expected) {
    assertEquals(new HashSet<>(Arrays.asList(expected)),
        new HashSet<>(database.exportServiceRequests()));
  }


  //ALL THE NODE TESTS
  @Test
  public void addSameNodeTest() {
    database.addNode("OELEV00101", 0, 0, "1", "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("OELEV00101", 0, 0, "1", "Faulkner", "ELEV", "Elevator 1", "Elev1");
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", testNodes[0]);
    map.put("OELEV00201", testNodes[1]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addMultipleNodesAutoIDTest() {
    val id1 = database.addNode(0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addLetterFloorNodesAutoIDTest() {
    val id1 = database.addNode(0, 0, "L", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "L", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addLetterFloorNodesAutoIDTest2() {
    val id1 = database.addNode(0, 0, "L", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "01", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "L", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "2", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "01", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "2", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "2", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addLetterFloorNodesAutoIDTest3() {
    val id1 = database.addNode(0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "L", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "L", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "L", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "L", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "L", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "L", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addLetterFloorNodesAutoIDTest4() {
    val id1 = database.addNode(0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "L1", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "L1", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "L1", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "1", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "L1", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "1", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "L1", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "L1", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
      System.out.println("=========================");
      System.out.println(database.exportNodes().get(n.getNodeID()).getFloor());
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addLetterFloorNodesAutoIDTest5() {
    val id1 = database.addNode(0, 0, "01", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    val id2 = database.addNode(9, 0, "L1", "Faulkner", "LABS", "Surgery", "Surgery");
    val id3 = database.addNode(0, 5, "01", "Faulkner", "ELEV", "Elevator X", "ElevX");
    val id4 = database.addNode(5, 7, "L1", "Faulkner", "LABS", "Something", "Something");
    val id5 = database.addNode(19, 77, "L1", "Faulkner", "LABS", "LabW", "LabW");
    Node N1 = new Node(id1, 0, 0, "01", "Faulkner", "DEPT", "Intensive Care Unit", "ICU");
    Node N2 = new Node(id2, 9, 0, "L1", "Faulkner", "LABS", "Surgery", "Surgery");
    Node N3 = new Node(id3, 0, 5, "01", "Faulkner", "ELEV", "Elevator X", "ElevX");
    Node N4 = new Node(id4, 5, 7, "L1", "Faulkner", "LABS", "Something", "Something");
    Node N5 = new Node(id5, 19, 77, "L1", "Faulkner", "LABS", "LabW", "LabW");
    Node[] testNodesAutoID = {N1, N2, N3, N4, N5, testNodes[0], testNodes[1], testNodes[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Map.Entry<String, Node> n : database.exportNodes().entrySet()) {
      idList.add(n.getValue().getNodeID());
    }
    for (Node n : testNodesAutoID) {
      assertTrue(idList.contains(n.getNodeID()));
    }
    assertEquals(8, database.exportNodes().size());
  }

  @Test
  public void addMultipleNodesTest() {
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", testNodes[0]);
    map.put("OELEV00201", testNodes[1]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addAndDeleteNodesTest() {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, "OELEV00201");
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", testNodes[0]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void failToDeleteNodesTest() {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_ID, "123");
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", testNodes[0]);
    map.put("OELEV00201", testNodes[1]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addManyAndUpdateNodeTest() {
    val updatedNode = new Node("OELEV00101", 3, 0, "1", "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, "OELEV00101", NodeProperty.X_COORD, 3);
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", updatedNode);
    map.put("OELEV00201", testNodes[1]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void failUpdateNodeTest() {
    database.update(Table.NODES_TABLE, NodeProperty.NODE_ID, "876", NodeProperty.X_COORD, 3);
    val map = new HashMap<String, Node>();
    map.put("OELEV00101", testNodes[0]);
    map.put("OELEV00201", testNodes[1]);
    map.put("OELEV00301", testNodes[2]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void exportEmptyNodeTest() {
    database.deleteFromTable(Table.NODES_TABLE, NodeProperty.NODE_TYPE, "ELEV");
    val map = new HashMap<String, Node>(); // tests empty list with database.export()
    assertEquals(map, database.exportNodes());
  }

  //ALL THE EDGE TESTS
  @Test
  public void addSameEdgeTest() {
    database.addEdge("01", "OELEV00101", "OELEV00201");
    checkResultEdges(testEdges);
  }

  @Test
  public void addMultipleEdgesAutoIDTest() {
    String id1 = database.addEdge("OELEV00201", "OELEV00101");
    String id2 = database.addEdge("OELEV00301", "OELEV00101");
    Edge E1 = new Edge(id1, "OELEV00201", "OELEV00101");
    Edge E2 = new Edge(id2, "OELEV00301", "OELEV00301");
    Edge[] testEdgesAutoID = {E1, E2, testEdges[0], testEdges[1], testEdges[2]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Edge e : database.exportEdges()) {
      idList.add(e.getEdgeID());
    }
    for (Edge e : testEdgesAutoID) {
      assertTrue(idList.contains(e.getEdgeID()));
    }
    assertEquals(5, database.exportEdges().size());
  }

  @Test
  public void addMultipleEdgesTest() {
    checkResultEdges(testEdges);
  }


  @Test
  public void addAndDeleteEdgesTest() {
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "01");
    checkResultEdges(testEdges[1], testEdges[2]);
  }

  @Test
  public void failToDeleteEdgesTest() {
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "123");
    checkResultEdges(testEdges);
  }

  @Test
  public void addManyAndUpdateEdgeTest() {
    val updatedEdge = new Edge("01", "OELEV00101", "OELEV00301");
    database
        .update(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "01", EdgeProperty.STOP_ID, "OELEV00301");
    checkResultEdges(updatedEdge, testEdges[1], testEdges[2]); //might need to change into a list
  }

  @Test
  public void failUpdateEdgeTest() {
    database.update(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "0123", EdgeProperty.STOP_ID,
        "OELEV00301");
    checkResultEdges(testEdges); //might need to change into a list
  }

  @Test
  public void exportEmptyEdgeTest() {
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "01");
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "02");
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "03");
    List<Edge> list = new LinkedList<>(); // tests empty list with database.export()
    assertEquals(list, database.exportEdges());
  }

  //ALL THE EMPLOYEE TESTS
  @Test
  public void addSameEmployeeTest() {
    database.addEmployee("01", "Jeff", "Gift", "password", false);
    checkResultEmployees(testEmployees);
  }

  @Test
  public void addMultipleEmployeesAutoIDTest() {
    String id1 = database.addEmployee("Steve", "Admin", "password", false);
    String id2 = database.addEmployee("Larry", "Admin", "password", true);
    String id3 = database.addEmployee("Jane", "Gift", "password", false);
    String id4 = database.addEmployee("Marie", "Interpreter", "password", true);
    Employee E1 = new Employee(id1, "Steve", "Admin", "password", false);
    Employee E2 = new Employee(id2, "Larry", "Admin", "password", true);
    Employee E3 = new Employee(id3, "Jane", "Gift", "password", false);
    Employee E4 = new Employee(id4, "Marie", "Interpreter", "password", true);
    Employee[] testEmployeesAutoID = {E1, E2, E3, E4, testEmployees[0], testEmployees[1],
        testEmployees[2], testEmployees[3]};
    LinkedList<String> idList = new LinkedList<String>();
    for (Employee e : database.exportEmployees()) {
      idList.add(e.getEmployeeID());
    }
    for (Employee e : testEmployeesAutoID) {
      assertTrue(idList.contains(e.getEmployeeID()));
    }
    assertEquals(8, database.exportEmployees().size());
  }

  @Test
  public void addMultipleEmployeesTest() {
    checkResultEmployees(testEmployees);
  }

  @Test
  public void addAndDeleteEmployeesTest() {
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "02");
    checkResultEmployees(testEmployees[0], testEmployees[2], testEmployees[3]);
  }

  @Test
  public void failToDeleteEmployeesTest() {
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "9876534");
    checkResultEmployees(testEmployees);
  }

  @Test
  public void addManyAndUpdateEmployeeTest() {
    Employee updatedEmployee = new Employee("01", "Jeff Sullivan", "Gift", "password", false);
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "01", EmployeeProperty.NAME,
        "Jeff Sullivan");
    checkResultEmployees(testEmployees[3], updatedEmployee, testEmployees[1], testEmployees[2]);
  }

  @Test
  public void failUpdateEmployeeTest() {
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "1", EmployeeProperty.NAME,
        "Jeff Sullivan");
    checkResultEmployees(testEmployees);
  }

  @Test
  public void exportEmptyEmployeeTest() {
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.TYPE, "Gift");
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.TYPE, "Interpreter");
    assertEquals(1, database.exportEmployees().size());
  }

  //ALL THE SERVICE REQUEST TESTS
  @Test
  public void addMultipleServiceRequestsTest() {
    ServiceRequestData sanitationData = new SanitationRequestData("Wet spill", "Coffee spill");
    val id1 = database.addServiceRequest("0500", "OELEV00101", "Sanitation", "Bob", sanitationData);
    val id2 = database
        .addServiceRequest("1000", "OELEV00201", "Sanitation", "Marcy", sanitationData);
    val id3 = database.addServiceRequest("1600", "OELEV00101", "Sanitation", "Deb", sanitationData);
    ServiceRequest SR1 = new ServiceRequest(id1, "0500", "OELEV00101", "Sanitation", "New", "Bob",
        "0",
        "0", sanitationData);
    ServiceRequest SR2 = new ServiceRequest(id2, "1000", "OELEV00201", "Sanitation", "New", "Marcy",
        "0",
        "0", sanitationData);
    ServiceRequest SR3 = new ServiceRequest(id3, "1600", "OELEV00101", "Sanitation", "New", "Deb",
        "0",
        "0", sanitationData);
    ServiceRequest[] testServiceRequests = {SR1, SR2, SR3};
    LinkedList<String> idList = new LinkedList<String>();
    for (ServiceRequest s : database.exportServiceRequests()) {
      idList.add(s.getRequestID());
    }
    for (ServiceRequest s : testServiceRequests) {
      assertTrue(idList.contains(s.getRequestID()));
    }
    assertEquals(3, database.exportServiceRequests().size());
    //checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void addAndDeleteServiceRequestsTest() {
    ServiceRequestData sanitationData = new SanitationRequestData("Wet spill", "Coffee spill");
    val id1 = database.addServiceRequest("0500", "OELEV00101", "Sanitation", "Bob", sanitationData);
    val id2 = database
        .addServiceRequest("1000", "OELEV00201", "Sanitation", "Marcy", sanitationData);
    val id3 = database.addServiceRequest("1600", "OELEV00101", "Sanitation", "Deb", sanitationData);
    ServiceRequest SR1 = new ServiceRequest(id1, "0500", "OELEV00101", "Sanitation", "New", "Bob",
        "0",
        "0", sanitationData);
    ServiceRequest SR2 = new ServiceRequest(id2, "1000", "OELEV00201", "Sanitation", "New", "Marcy",
        "0",
        "0", sanitationData);
    ServiceRequest SR3 = new ServiceRequest(id3, "1600", "OELEV00101", "Sanitation", "New", "Deb",
        "0",
        "0", sanitationData);
    ServiceRequest[] testServiceRequests = {SR1, SR3};
    database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, id2);
    LinkedList<String> idList = new LinkedList<String>();
    for (ServiceRequest s : database.exportServiceRequests()) {
      idList.add(s.getRequestID());
    }
    for (ServiceRequest s : testServiceRequests) {
      assertTrue(idList.contains(s.getRequestID()));
    }
    assertEquals(2, database.exportServiceRequests().size());
    //assertEquals(Arrays.asList(testServiceRequests), database.exportServiceRequests());
    //checkResultServiceRequests(testServiceRequests[0], testServiceRequests[2]);
  }

  @Test
  public void failToDeleteServiceRequestsTest() {
    ServiceRequestData sanitationData = new SanitationRequestData("Wet spill", "Coffee spill");
    String id1 = database
        .addServiceRequest("0500", "OELEV00101", "Sanitation", "Bob", sanitationData);
    String id2 = database
        .addServiceRequest("1000", "OELEV00201", "Sanitation", "Marcy", sanitationData);
    String id3 = database
        .addServiceRequest("1600", "OELEV00101", "Sanitation", "Deb", sanitationData);
    ServiceRequest SR1 = new ServiceRequest(id1, "0500", "OELEV00101", "Sanitation", "New", "Bob",
        "0",
        "0", sanitationData);
    ServiceRequest SR2 = new ServiceRequest(id2, "1000", "OELEV00201", "Sanitation", "New", "Marcy",
        "0",
        "0", sanitationData);
    ServiceRequest SR3 = new ServiceRequest(id3, "1600", "OELEV00101", "Sanitation", "New", "Deb",
        "0",
        "0", sanitationData);
    ServiceRequest[] testServiceRequests = {SR1, SR2, SR3};
    database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "1");
    LinkedList<String> idList = new LinkedList<String>();
    for (ServiceRequest s : database.exportServiceRequests()) {
      idList.add(s.getRequestID());
    }
    for (ServiceRequest s : testServiceRequests) {
      assertTrue(idList.contains(s.getRequestID()));
    }
    assertEquals(3, database.exportServiceRequests().size());
    //checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void addManyAndUpdateServiceRequestTest() {
    ServiceRequestData sanitationData = new SanitationRequestData("Wet spill", "Coffee spill");
    String id1 = database
        .addServiceRequest("0500", "OELEV00101", "Sanitation", "Bob", sanitationData);
    String id2 = database
        .addServiceRequest("1000", "OELEV00201", "Sanitation", "Marcy", sanitationData);
    String id3 = database
        .addServiceRequest("1600", "OELEV00101", "Sanitation", "Deb", sanitationData);
    ServiceRequest SR1 = new ServiceRequest(id1, "0500", "OELEV00101", "Sanitation", "New", "Bob",
        "0",
        "0", sanitationData);
    ServiceRequest SR2 = new ServiceRequest(id2, "1000", "OELEV00201", "Sanitation", "New", "Marcy",
        "0",
        "0", sanitationData);
    ServiceRequest SR3 = new ServiceRequest(id3, "1600", "OELEV00101", "Sanitation", "New", "Deb",
        "0",
        "0", sanitationData);
    ServiceRequest updatedServiceRequest = new ServiceRequest(id3, "1600", "OELEV00301",
        "Sanitation",
        "New",
        "Deb", "0", "0", sanitationData);
    ServiceRequest[] testServiceRequests = {SR1, SR2, updatedServiceRequest};
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, id3,
        ServiceRequestProperty.REQUEST_NODE, "OELEV00301");
    LinkedList<String> idList = new LinkedList<String>();
    for (ServiceRequest s : database.exportServiceRequests()) {
      idList.add(s.getRequestID());
    }
    for (ServiceRequest s : testServiceRequests) {
      assertTrue(idList.contains(s.getRequestID()));
    }
    assertEquals(3, database.exportServiceRequests().size());
    //checkResultServiceRequests(testServiceRequests[0], testServiceRequests[1], updatedServiceRequest);
  }

  @Test
  public void failUpdateServiceRequestsTest() {
    ServiceRequestData sanitationData = new SanitationRequestData("Wet spill", "Coffee spill");
    String id1 = database
        .addServiceRequest("0500", "OELEV00101", "Sanitation", "Bob", sanitationData);
    String id2 = database
        .addServiceRequest("1000", "OELEV00201", "Sanitation", "Marcy", sanitationData);
    String id3 = database
        .addServiceRequest("1600", "OELEV00101", "Sanitation", "Deb", sanitationData);
    ServiceRequest SR1 = new ServiceRequest(id1, "0500", "OELEV00101", "Sanitation", "New", "Bob",
        "0",
        "0", sanitationData);
    ServiceRequest SR2 = new ServiceRequest(id2, "1000", "OELEV00201", "Sanitation", "New", "Marcy",
        "0",
        "0", sanitationData);
    ServiceRequest SR3 = new ServiceRequest(id3, "1600", "OELEV00101", "Sanitation", "New", "Deb",
        "0",
        "0", sanitationData);
    ServiceRequest[] testServiceRequests = {SR1, SR2, SR3};
    ServiceRequest updatedServiceRequest = new ServiceRequest(id3, "1600", "OELEV00301",
        "Sanitation",
        "New",
        "Deb", "0", "0", sanitationData);
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "98",
        ServiceRequestProperty.REQUEST_NODE, "OELEV00301");
    LinkedList<String> idList = new LinkedList<String>();
    for (ServiceRequest s : database.exportServiceRequests()) {
      idList.add(s.getRequestID());
    }
    for (ServiceRequest s : testServiceRequests) {
      assertTrue(idList.contains(s.getRequestID()));
    }
    assertEquals(3, database.exportServiceRequests().size());
    //checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void exportEmptyServiceRequestTest() {
    database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.WHO_MARKED, "0");
    List<ServiceRequest> list = new LinkedList<>(); // tests empty list with database.export()
    assertEquals(list, database.exportServiceRequests());
  }


  //ALL THE SCHEDULER TESTS
  @Test
  public void addMultipleSchedulers() {
    val id1 = database.addScheduler("01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    val id2 = database.addScheduler("02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    val id3 = database.addScheduler("03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler S1 = new Scheduler(id1, "01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    Scheduler S2 = new Scheduler(id2, "02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    Scheduler S3 = new Scheduler(id3, "03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler[] testSchedulers = {S1, S2, S3};
    LinkedList<String> idList = new LinkedList<String>();
    for (Scheduler s : database.exportScheduler()) {
      idList.add(s.getSchedulerID());
    }
    for (Scheduler s : testSchedulers) {
      assertTrue(idList.contains(s.getSchedulerID()));
    }
    assertEquals(3, database.exportScheduler().size());
  }

  @Test
  public void addAndDeleteScheduler() {
    val id1 = database.addScheduler("01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    val id2 = database.addScheduler("02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    val id3 = database.addScheduler("03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler S1 = new Scheduler(id1, "01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    Scheduler S3 = new Scheduler(id3, "03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    database.deleteFromTable(Table.SCHEDULER_TABLE, SchedulerProperty.SCHEDULER_ID, id2);
    Scheduler[] testSchedulers = {S1, S3};
    LinkedList<String> idList = new LinkedList<String>();
    for (Scheduler s : database.exportScheduler()) {
      idList.add(s.getSchedulerID());
    }
    for (Scheduler s : testSchedulers) {
      assertTrue(idList.contains(s.getSchedulerID()));
    }
    assertEquals(2, database.exportScheduler().size());
  }

  @Test
  public void failToDeleteScheduler() {
    val id1 = database.addScheduler("01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    val id2 = database.addScheduler("02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    val id3 = database.addScheduler("03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler S1 = new Scheduler(id1, "01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    Scheduler S2 = new Scheduler(id2, "02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    Scheduler S3 = new Scheduler(id3, "03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler[] testSchedulers = {S1, S2, S3};
    database.deleteFromTable(Table.SCHEDULER_TABLE, SchedulerProperty.SCHEDULER_ID, "89374562345");
    LinkedList<String> idList = new LinkedList<String>();
    for (Scheduler s : database.exportScheduler()) {
      idList.add(s.getSchedulerID());
    }
    for (Scheduler s : testSchedulers) {
      assertTrue(idList.contains(s.getSchedulerID()));
    }
    assertEquals(3, database.exportScheduler().size());
  }

  @Test
  public void addManyAndUpdateScheduler() {
    val id1 = database.addScheduler("01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    val id2 = database.addScheduler("02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    val id3 = database.addScheduler("03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler S1 = new Scheduler(id1, "01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    Scheduler S2 = new Scheduler(id2, "03", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    Scheduler S3 = new Scheduler(id3, "03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    database.update(Table.SCHEDULER_TABLE, SchedulerProperty.SCHEDULER_ID, id2,
        SchedulerProperty.EMPLOYEE_ID, "03");
    Scheduler[] testSchedulers = {S1, S2, S3};
    LinkedList<String> idList = new LinkedList<String>();
    for (Scheduler s : database.exportScheduler()) {
      idList.add(s.getSchedulerID());
    }
    for (Scheduler s : testSchedulers) {
      assertTrue(idList.contains(s.getSchedulerID()));
    }
    assertEquals(3, database.exportScheduler().size());
  }

  @Test
  public void failUpdateScheduler() {
    val id1 = database.addScheduler("01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    val id2 = database.addScheduler("02", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    val id3 = database.addScheduler("03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    Scheduler S1 = new Scheduler(id1, "01", "05:04:2020 21:00", "05:04:2020 21:00", "On Call");
    Scheduler S2 = new Scheduler(id2, "03", "05:16:2020 21:00", "05:16:2020 23:00", "On Call");
    Scheduler S3 = new Scheduler(id3, "03", "05:04:2020 21:00", "05:04:2020 21:00", "Reflection 3");
    database.update(Table.SCHEDULER_TABLE, SchedulerProperty.SCHEDULER_ID, "75436542",
        SchedulerProperty.EMPLOYEE_ID, "03");
    Scheduler[] testSchedulers = {S1, S2, S3};
    LinkedList<String> idList = new LinkedList<String>();
    for (Scheduler s : database.exportScheduler()) {
      idList.add(s.getSchedulerID());
    }
    for (Scheduler s : testSchedulers) {
      assertTrue(idList.contains(s.getSchedulerID()));
    }
    assertEquals(3, database.exportScheduler().size());
  }

  @Test
  public void exportEmptySchedulerTest() {
    List<Scheduler> list = new LinkedList<>(); // tests empty list with database.export()
    assertEquals(list, database.exportScheduler());
  }

  //TESTS FOR EMPLOYEE NAME TO ID METHOD
  @Test
  @Deprecated
  public void convertEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", "password", true);
    assertEquals("Fred", database.employeeNameFromID("0123"));
  }

  @Test
  @Deprecated
  public void chooseCorrectEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", "password", true);
    database.addEmployee("345", "Maria", "Gift", "password", false);
    database.addEmployee("4598", "Paul", "Interpreter", "password", true);
    assertEquals("Maria", database.employeeNameFromID("345"));
  }

  @Test
  @Deprecated
  public void invalidEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", "password", true);
    database.addEmployee("345", "Maria", "Gift", "password", false);
    assertEquals("Failed to find name", database.employeeNameFromID("12345"));
  }

}
