package edu.wpi.cs3733.d20.teamO.model.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.val;
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

  private final Edge[] testEdges = {
      new Edge("01", "0101", "0102"),
      new Edge("02", "0101", "0103"),
      new Edge("03", "0102", "0103"),
  };

  private final Employee[] testEmployees = {
      new Employee("01", "Jeff", "Gift", false),
      new Employee("02", "Jeff", "Gift", false),
      new Employee("03", "Liz", "Interpreter", true),
  };

  private final ServiceRequest[] testServiceRequests = {
      new ServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02"),
      new ServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03"),
      new ServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02"),
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
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    val map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addMultipleNodesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    val map = new HashMap<String, Node>();
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
    val map = new HashMap<String, Node>();
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
    val map = new HashMap<String, Node>();
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
    val map = new HashMap<String, Node>();
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
    val map = new HashMap<String, Node>();
    map.put("0101", testNodes[0]);
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void exportEmptyNodeTest() {
    HashMap map = new HashMap<String, Node>(); // tests empty list with database.export()
    assertEquals(map, database.exportNodes());
  }

  @Test
  public void addSameEdgeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addEdge("01", "0101", "0102");
    database.addEdge("01", "0101", "0102");
    checkResultEdges(testEdges[0]);
  }

  @Test
  public void addMultipleEdgesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 4, 1, "Roberts", "STAIR", "Stairs 1", "Stairs1");
    database.addEdge("01", "0101", "0102");
    database.addEdge("02", "0101", "0103");
    checkResultEdges(testEdges[0], testEdges[1]);
  }


  @Test
  public void addAndDeleteEdgesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEdge("01", "0101", "0102");
    database.addEdge("02", "0101", "0103");
    database.addEdge("03", "0102", "0103");
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "01");
    checkResultEdges(testEdges[1], testEdges[2]);
  }

  @Test
  public void failToDeleteEdgesTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEdge("01", "0101", "0102");
    database.addEdge("02", "0101", "0103");
    database.addEdge("03", "0102", "0103");
    database.deleteFromTable(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "123");
    checkResultEdges(testEdges);
  }

  @Test
  public void addManyAndUpdateEdgeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEdge("01", "0101", "0102");
    val updatedEdge = new Edge("01", "0101", "0103");
    database.update(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "01", EdgeProperty.STOP_ID, "0103");
    checkResultEdges(updatedEdge); //might need to change into a list
  }

  @Test
  public void failUpdateEdgeTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEdge("01", "0101", "0102");
    val updatedEdge = new Edge("01", "0101", "0103");
    database.update(Table.EDGES_TABLE, EdgeProperty.EDGE_ID, "0123", EdgeProperty.STOP_ID, "0103");
    checkResultEdges(testEdges[0]); //might need to change into a list
  }

  @Test
  public void exportEmptyEdgeTest() {
    List list = new LinkedList(); // tests empty list with database.export()
    assertEquals(list, database.exportEdges());
  }

  @Test
  public void addSameEmployeeTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("01", "Jeff", "Gift", false);
    checkResultEmployees(testEmployees[0]);
  }

  @Test
  public void addMultipleEmployeesTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    checkResultEmployees(testEmployees);
  }

  @Test
  public void addAndDeleteEmployeesTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "02");
    checkResultEmployees(testEmployees[0], testEmployees[2]);
  }

  @Test
  public void failToDeleteEmployeesTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "9876534");
    checkResultEmployees(testEmployees);
  }

  @Test
  public void addManyAndUpdateEmployeeTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    Employee updatedEmployee = new Employee("01", "Jeff Sullivan", "Gift", false);
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "01", EmployeeProperty.NAME,
        "Jeff Sullivan");
    checkResultEmployees(updatedEmployee, testEmployees[1], testEmployees[2]);
  }

  @Test
  public void failUpdateEmployeeTest() {
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    Employee updatedEmployee = new Employee("01", "Jeff Sullivan", "Gift", false);
    database.update(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "1", EmployeeProperty.NAME,
        "Jeff Sullivan");
    checkResultEmployees(testEmployees);
  }

  @Test
  public void exportEmptyEmployeeTest() {
    List list = new LinkedList(); // tests empty list with database.export()
    assertEquals(list, database.exportEmployees());
  }

  @Test
  public void addSameServiceRequestTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    checkResultServiceRequests(testServiceRequests[2]);
  }

  @Test
  public void addMultipleServiceRequestsTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02");
    database.addServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void addAndDeleteServiceRequestsTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02");
    database.addServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "02");
    checkResultServiceRequests(testServiceRequests[0], testServiceRequests[2]);
  }

  @Test
  public void failToDeleteServiceRequestsTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02");
    database.addServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    database
        .deleteFromTable(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "982654");
    checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void addManyAndUpdateServiceRequestTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02");
    database.addServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    ServiceRequest updatedServiceRequest = new ServiceRequest("03", "0900", "0102", "Gift", "Larry",
        "01", "02");
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "03",
        ServiceRequestProperty.REQUEST_NODE, "0102");
    checkResultServiceRequests(testServiceRequests[0], testServiceRequests[1],
        updatedServiceRequest);
  }

  @Test
  public void failUpdateServiceRequestsTest() {
    database.addNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1", "Elev1");
    database.addNode("0102", 9, 0, 1, "Faulkner", "ELEV", "Elevator 2", "Elev2");
    database.addNode("0103", 0, 5, 1, "Faulkner", "ELEV", "Elevator 3", "Elev3");
    database.addEmployee("01", "Jeff", "Gift", false);
    database.addEmployee("02", "Jeff", "Gift", false);
    database.addEmployee("03", "Liz", "Interpreter", true);
    database.addServiceRequest("01", "10", "0101", "Gift", "Paul", "01", "02");
    database.addServiceRequest("02", "0500", "0102", "Interpreter", "Jane", "01", "03");
    database.addServiceRequest("03", "0900", "0101", "Gift", "Larry", "01", "02");
    ServiceRequest updatedServiceRequest = new ServiceRequest("03", "0900", "0102", "Gift", "Larry",
        "01", "02");
    database.update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.REQUEST_ID, "04",
        ServiceRequestProperty.REQUEST_NODE, "0102");
    checkResultServiceRequests(testServiceRequests);
  }

  @Test
  public void exportEmptyServiceRequestTest() {
    List list = new LinkedList(); // tests empty list with database.export()
    assertEquals(list, database.exportServiceRequests());
  }

  @Test
  public void convertEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", true);
    assertEquals("Fred", database.employeeNameFromID("0123"));
  }

  @Test
  public void chooseCorrectEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", true);
    database.addEmployee("345", "Maria", "Gift", false);
    database.addEmployee("4598", "Paul", "Interpreter", true);
    assertEquals("Maria", database.employeeNameFromID("345"));
  }

  @Test
  public void invalidEmployeeIDtoNameTest() {
    database.addEmployee("0123", "Fred", "Gift", true);
    database.addEmployee("345", "Maria", "Gift", false);
    assertEquals("Failed to find name", database.employeeNameFromID("12345"));
  }

}
