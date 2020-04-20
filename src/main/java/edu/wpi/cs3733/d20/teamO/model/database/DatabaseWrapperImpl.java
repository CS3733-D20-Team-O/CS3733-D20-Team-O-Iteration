package edu.wpi.cs3733.d20.teamO.model.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.TableProperty;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * The default implementation for a DatabaseWrapper
 */
@Slf4j
class DatabaseWrapperImpl implements DatabaseWrapper {

  /**
   * The connection to use for database operations
   */
  private final Connection connection;

  @Inject
  public DatabaseWrapperImpl(Connection connection) {
    this.connection = connection;
    init();
  }

  /**
   * Checks whether the supplied table is not initialized
   *
   * @param table the table to check for
   * @return whether or note the table is not initialized
   */
  private boolean isNotInitialized(Table table) {
    try {
      val tableName = table.getTableName();
      return !connection.getMetaData().getTables(null, null, tableName, null).next();
    } catch (SQLException e) {
      log.warn("SQLException thrown while checking if table '" + table + "' was initialized", e);
      return true;
    }
  }

  /**
   * Initializes the database
   */
  private void init() {
    // Initialize the nodes table if not initialized
    if (isNotInitialized(Table.NODES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.NODES_TABLE
            + "(" + NodeProperty.NODE_ID.getColumnName() + " VARCHAR(255), "
            + NodeProperty.X_COORD.getColumnName() + " INT, "
            + NodeProperty.Y_COORD.getColumnName() + " INT, "
            + NodeProperty.FLOOR.getColumnName() + " INT, "
            + NodeProperty.BUILDING.getColumnName() + " VARCHAR(255), "
            + NodeProperty.NODE_TYPE.getColumnName() + " VARCHAR(255), "
            + NodeProperty.LONG_NAME.getColumnName() + " VARCHAR(255), "
            + NodeProperty.SHORT_NAME.getColumnName() + " VARCHAR(255), "
            + "PRIMARY KEY (" + NodeProperty.NODE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.NODES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.NODES_TABLE, e);
      }
    }

    // Initialize the edges table if not initialized
    if (isNotInitialized(Table.EDGES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EDGES_TABLE
            + "(" + EdgeProperty.EDGE_ID.getColumnName() + " VARCHAR(255), "
            + EdgeProperty.START_ID.getColumnName() + " VARCHAR(255) REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName() + "), "
            + EdgeProperty.STOP_ID.getColumnName() + " VARCHAR(255) REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName() + "), "
            + "PRIMARY KEY (" + EdgeProperty.EDGE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.EDGES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EDGES_TABLE, e);
      }
    }

    // Initialize the employee table if not initialized
    if (isNotInitialized(Table.EMPLOYEE_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EMPLOYEE_TABLE
            + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + " VARCHAR(255), "
            + EmployeeProperty.NAME.getColumnName() + " VARCHAR(255), "
            + EmployeeProperty.TYPE.getColumnName() + " VARCHAR(255), "
            + EmployeeProperty.IS_AVAILABLE.getColumnName() + " BOOLEAN, "
            + "PRIMARY KEY (" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.EMPLOYEE_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EMPLOYEE_TABLE, e);
      }
    }

    // Initialize the service requests table if not initialized
    if (isNotInitialized(Table.SERVICE_REQUESTS_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.SERVICE_REQUESTS_TABLE
            + "(" + ServiceRequestProperty.REQUEST_ID.getColumnName() + " VARCHAR(255), "
            + ServiceRequestProperty.REQUEST_TIME.getColumnName() + " VARCHAR(255), "
            + ServiceRequestProperty.REQUEST_NODE.getColumnName() + " VARCHAR(255) REFERENCES "
            + Table.NODES_TABLE + "(" + NodeProperty.NODE_ID.getColumnName() + "), "
            + ServiceRequestProperty.TYPE.getColumnName() + " VARCHAR(255), "
            + ServiceRequestProperty.REQUESTER_NAME.getColumnName() + " VARCHAR(255), "
            + ServiceRequestProperty.WHO_MARKED.getColumnName() + " VARCHAR(255) REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "), "
            + ServiceRequestProperty.EMPLOYEE_ASSIGNED.getColumnName() + " VARCHAR(255) REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "), "
            + "PRIMARY KEY (" + ServiceRequestProperty.REQUEST_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.SERVICE_REQUESTS_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.SERVICE_REQUESTS_TABLE, e);
      }
    }
  }

  @Override
  public int addNode(String nodeID, int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName) {
    val query = "INSERT into " + Table.NODES_TABLE.getTableName() +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, nodeID);
      stmt.setInt(2, xCoord);
      stmt.setInt(3, yCoord);
      stmt.setInt(4, floor);
      stmt.setString(5, building);
      stmt.setString(6, nodeType);
      stmt.setString(7, longName);
      stmt.setString(8, shortName);
      val nodesAffected = stmt.executeUpdate();
      log.info("Added node with ID " + nodeID);
      log.debug("Result of add node was " + nodesAffected);
      return nodesAffected;
    } catch (SQLException e) {
      log.error("Failed to add a new node with ID " + nodeID, e);
      return -1;
    }
  }

  @Override
  public int addEdge(String edgeID, String startNodeID, String stopNodeID) {
    val query = "INSERT into " + Table.EDGES_TABLE.getTableName() + " VALUES (?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, edgeID);
      stmt.setString(2, startNodeID);
      stmt.setString(3, stopNodeID);
      val edgesAffected = stmt.executeUpdate();
      log.info("Added edge with ID " + edgeID);
      log.debug("Result of add edge was " + edgesAffected);
      return edgesAffected;
    } catch (SQLException e) {
      log.error("Failed to add a new edge with ID " + edgeID, e);
      return -1;
    }
  }

  @Override
  public int addServiceRequest(String requestID, String requestTime, String requestNode,
      String type, String requesterName, String whoMarked, String employeeAssigned) {
    val query = "INSERT into " + Table.SERVICE_REQUESTS_TABLE
        + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, requestID);
      stmt.setString(2, requestTime);
      stmt.setString(3, requestNode);
      stmt.setString(4, type);
      stmt.setString(5, requesterName);
      stmt.setString(6, whoMarked);
      stmt.setString(7, employeeAssigned);
      val requestsAffected = stmt.executeUpdate();
      log.info("Added service request with ID " + requestID);
      log.debug("Result of add service request was " + requestsAffected);
      return requestsAffected;
    } catch (SQLException e) {
      log.error("Failed to add a new service request with ID " + requestID, e);
      return -1;
    }
  }

  @Override
  public int addEmployee(String employeeID, String name, String type, boolean isAvailable) {
    val query = "INSERT into " + Table.EMPLOYEE_TABLE.getTableName() + " VALUES (?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, employeeID);
      stmt.setString(2, name);
      stmt.setString(3, type);
      stmt.setBoolean(4, isAvailable);
      val employeesAffected = stmt.executeUpdate();
      log.info("Added employee with ID " + employeeID);
      log.debug("Result of add employee was " + employeesAffected);
      return employeesAffected;
    } catch (SQLException e) {
      log.error("Failed to add a new employee with ID " + employeeID, e);
      return -1;
    }
  }

  @Override
  public int deleteMatching(TableProperty matchingProperty, String matching) {
    val query = "DELETE from " + table.getTableName() +
        " WHERE " + property.getColumnName() + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, matching);
      val affected = stmt.executeUpdate();
      log.info("Deleted " + affected + " record(s) from " + table.getTableName());
      log.debug("Result of deletion was " + affected);
      return affected;
    } catch (SQLException e) {
      val error = "Failed to delete record(s) from " + table.getTableName() +
          " using property " + property.getColumnName() + " and matching " + matching;
      log.error(error, e);
      return -1;
    }
  }

  @Override
  public int update(TableProperty matchingProperty, String matching, TableProperty dataProperty,
      String data) {
    val query = "UPDATE " + table.getTableName() + " set "
        + newInfoProperty.getColumnName() + " = ? "
        + "WHERE " + property.getColumnName() + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, data);
      stmt.setString(2, id);
      val affected = stmt.executeUpdate();
      log.info("Updated " + affected + " record(s) from " + table.getTableName());
      log.debug("Result of update was " + affected);
      return affected;
    } catch (SQLException e) {
      val error = "Failed to update record(s) from " + table.getTableName() +
          " using property " + property.getColumnName() + " and matching " + id;
      log.error(error, e);
      return -1;
    }
  }

  @Override
  public Map<String, Node> exportNodes() {
    val map = new HashMap<String, Node>();
    val query = "SELECT * from " + Table.NODES_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        map.put(rset.getString(1), new Node(
            rset.getString(1),
            rset.getInt(2),
            rset.getInt(3),
            rset.getInt(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8)));
      }
    } catch (SQLException e) {
      log.error("Failed to export nodes", e);
    }
    for (val edge : exportEdges()) {
      val start = map.get(edge.getStartID());
      val stop = map.get(edge.getStopID());
      start.getNeighbors().add(stop);
      stop.getNeighbors().add(start);
    }
    return map;
  }

  @Override
  public List<Edge> exportEdges() {
    val edges = new LinkedList<Edge>();
    val query = "SELECT * from " + Table.EDGES_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        edges.add(new Edge(
            rset.getString(1),
            rset.getString(2),
            rset.getString(3)));
      }
    } catch (SQLException e) {
      log.error("Failed to export edges", e);
    }
    return edges;
  }

  @Override
  public List<ServiceRequest> exportServiceRequests() {
    val serviceRequests = new LinkedList<ServiceRequest>();
    val query = "SELECT * from " + Table.SERVICE_REQUESTS_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        serviceRequests.add(new ServiceRequest(
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7)));
      }
    } catch (SQLException e) {
      log.error("Failed to export service requests", e);
    }
    return serviceRequests;
  }

  @Override
  public List<Employee> exportEmployees() {
    val employees = new LinkedList<Employee>();
    val query = "SELECT * from " + Table.EMPLOYEE_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        employees.add(new Employee(
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getBoolean(4)));
      }
    } catch (SQLException e) {
      log.error("Failed to export employees", e);
    }
    return employees;
  }
}