package edu.wpi.cs3733.d20.teamO.model.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.NodeProperty;
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
   * Checks whether the supplied table is initialized
   *
   * @param table the table to check for
   * @return whether or note the table is initialized
   */
  private boolean isInitialized(Table table) {
    try {
      val tableName = table.getTableName();
      return connection.getMetaData().getTables(null, null, tableName, null).next();
    } catch (SQLException e) {
      log.warn("SQLException thrown while checking if table '" + table + "' was initialized", e);
      return false;
    }
  }

  /**
   * Initializes the database
   */
  private void init() {
    // Initialize the nodes table if not initialized
    if (!isInitialized(Table.NODES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.NODES_TABLE
            + "(nodeID VARCHAR(255), "
            + "xCoord INT, "
            + "yCoord INT, "
            + "floor INT, "
            + "building VARCHAR(255), "
            + "nodeType VARCHAR(255), "
            + "longName VARCHAR(255), "
            + "shortName VARCHAR(255), "
            + "CONSTRAINT NODES_PK PRIMARY KEY (nodeID))";
        stmt.execute(query);
        log.info("Table " + Table.NODES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.NODES_TABLE, e);
      }
    }

    // Initialize the edges table if not initialized
    if (!isInitialized(Table.EDGES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EDGES_TABLE
            + "(edgeID VARCHAR(255), "
            + "startID VARCHAR(255) REFERENCES " + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID
            .getColumnName() + "), "
            + "stopID VARCHAR(255) REFERENCES " + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID
            .getColumnName() + "), "
            + "PRIMARY KEY (edgeID))";
        stmt.execute(query);
        log.info("Table " + Table.EDGES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EDGES_TABLE, e);
      }
    }

    // Initialize the employee table if not initialized
    if (!isInitialized(Table.EMPLOYEE_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EMPLOYEE_TABLE
            + "(employeeID VARCHAR(255), "
            + "name VARCHAR(255), "
            + "isAvailable BOOLEAN, "
            + "type VARCHAR(255), "
            + "PRIMARY KEY (employeeID))";
        stmt.execute(query);
        log.info("Table " + Table.EMPLOYEE_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EMPLOYEE_TABLE, e);
      }
    }

    // Initialize the service requests table if not initialized
    if (!isInitialized(Table.SERVICE_REQUESTS_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.SERVICE_REQUESTS_TABLE
            + "(requestID VARCHAR(255), "
            + "requestTime TIMESTAMP, "
            + "requestNode VARCHAR(255) REFERENCES " + Table.NODES_TABLE + "("
            + NodeProperty.NODE_ID.getColumnName() + "), "
            + "type VARCHAR(255), "
            + "requesterName VARCHAR(255), "
            + "whoMarked VARCHAR(255) REFERENCES " + Table.EMPLOYEE_TABLE + "("
            + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "), "
            + "employeeAssigned VARCHAR(255) REFERENCES " + Table.EMPLOYEE_TABLE + "("
            + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "), "
            + "PRIMARY KEY (requestID))";
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
  public int addEmployee(String employeeID, String name, String type, boolean isAvailable) {
    //todo (required for csv import of employee objects)
    return -1;
  }

  public int addServiceRequest(String requestID, String type) {
    //todo
    return -1;
  }

  @Override
  public int addServiceRequest(String requestID, String requestTime, String requestNode,
      String type, String requesterName, String whoMarked, String employeeAssigned) {
    //todo (required for csv import of serviceRequest objects)
    return -1;
  }

  /**
   * Deletes record(s) (example: a node or edge) from the specified table
   *
   * @param table    the table to delete the record(s) from
   * @param property the property of the table to use for deletion (typically should be ID)
   * @param matching the string of what to delete from the database (typically should be ID)
   * @return the number of affected entries
   */
  @Override
  public int deleteFromTable(Table table, TableProperty property, String matching) {
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

  /**
   * Updates a String in a record of the specified column of the specified table
   *
   * @param table    the table to perform the update on
   * @param property the property (column) for the update
   * @param id       the id of the record to update
   * @param data     the new data for the specified property
   * @return the number of affected entries
   */
  @Override
  public int update(Table table, TableProperty property, String id, String data) {
    // todo
    // Old code for reference:
//    val query = "UPDATE " + TABLE_NAME + " set "
//        + TABLE_NAME + ".nodeID = ?, "
//        + TABLE_NAME + ".xCoord = ?, "
//        + TABLE_NAME + ".yCoord = ?, "
//        + TABLE_NAME + ".floor = ?, "
//        + TABLE_NAME + ".building = ?, "
//        + TABLE_NAME + ".nodeType = ?, "
//        + TABLE_NAME + ".longName = ?, "
//        + TABLE_NAME + ".shortName = ? "
//        + "WHERE " + TABLE_NAME + ".nodeID = ?";
//    try (val stmt = connection.prepareStatement(query)) {
//      stmt.setString(1, node.getNodeID());
//      stmt.setInt(2, node.getXCoord());
//      stmt.setInt(3, node.getYCoord());
//      stmt.setInt(4, node.getFloor());
//      stmt.setString(5, node.getBuilding());
//      stmt.setString(6, node.getNodeType());
//      stmt.setString(7, node.getLongName());
//      stmt.setString(8, node.getShortName());
//      stmt.setString(9, nodeID);
//      if (stmt.executeUpdate() == 1) {
//        log.info("Updated node with ID " + nodeID);
//      } else {
//        log.error("Failed to update node with ID " + nodeID);
//      }
//    } catch (SQLException e) {
//      log.error("Failed to update node with ID " + nodeID, e);
//    }
    return 0;
  }

  /**
   * Updates an int in a record of the specified column of the specified table
   *
   * @param table    the table to perform the update on
   * @param property the property (column) for the update
   * @param id       the id of the record to update
   * @param data     the new data for the specified property
   * @return the number of affected entries
   */
  @Override
  public int update(Table table, TableProperty property, String id, int data) {
    // todo GREG-play around with removing this (and just cast int to string) to remove this method
    // todo
    return 0;
  }

  /**
   * @return a map of all nodeIDs stored in this database to their corresponding Nodes
   */
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
      log.error("Failed to export records", e);
    }
    // for each node in result set
    //   map.put(nodeID, node);
    // Victoria -- I did this edge parsing part for you. Just make code for the pseudocode above
    for (val edge : exportEdges()) {
      val start = map.get(edge.getStartNodeID());
      val stop = map.get(edge.getStopNodeID());
      start.getNeighbors().add(stop);
      stop.getNeighbors().add(start);
    }
    return map;
  }

  /**
   * Gets the edges of this database. Should only be used for exporting the database, not for A*!
   *
   * @return a list of the edges this database contains
   */
  @Override
  public List<Edge> exportEdges() {
    val edges = new LinkedList<Edge>();
    // todo pseudocode below
    // select all edges
    // for each edge in result set
    //   add edge to edges
    return edges;
  }

  @Override
  public List<ServiceRequest> exportServiceRequests() {
    return null;
  }

  @Override
  public List<Employee> exportEmployees() {
    return null;
  }
}