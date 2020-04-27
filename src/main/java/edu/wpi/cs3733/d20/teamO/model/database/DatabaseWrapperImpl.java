package edu.wpi.cs3733.d20.teamO.model.database;

import com.google.gson.Gson;
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
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
   * @return whether or not the table is not initialized
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
            + "(" + NodeProperty.NODE_ID.getColumnName() + " LONG VARCHAR, "
            + NodeProperty.X_COORD.getColumnName() + " INT, "
            + NodeProperty.Y_COORD.getColumnName() + " INT, "
            + NodeProperty.FLOOR.getColumnName() + " INT, "
            + NodeProperty.BUILDING.getColumnName() + " LONG VARCHAR, "
            + NodeProperty.NODE_TYPE.getColumnName() + " LONG VARCHAR, "
            + NodeProperty.LONG_NAME.getColumnName() + " LONG VARCHAR, "
            + NodeProperty.SHORT_NAME.getColumnName() + " LONG VARCHAR, "
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
            + "(" + EdgeProperty.EDGE_ID.getColumnName() + " LONG VARCHAR, "
            + EdgeProperty.START_ID.getColumnName() + " LONG VARCHAR REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + EdgeProperty.STOP_ID.getColumnName() + " LONG VARCHAR REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
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
            + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + " LONG VARCHAR, "
            + EmployeeProperty.NAME.getColumnName() + " LONG VARCHAR, "
            + EmployeeProperty.TYPE.getColumnName() + " LONG VARCHAR, "
            + EmployeeProperty.IS_AVAILABLE.getColumnName() + " LONG VARCHAR, "
            + "PRIMARY KEY (" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.EMPLOYEE_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EMPLOYEE_TABLE, e);
      }
      val query = "INSERT into " + Table.EMPLOYEE_TABLE.getTableName() + " VALUES (?, ?, ?, ?)";
      try (val stmt = connection.prepareStatement(query)) {
        stmt.setString(1, "0");
        stmt.setString(2, "");
        stmt.setString(3, "");
        stmt.setBoolean(4, false);
        stmt.executeUpdate(query);
        log.info("Added NULL employee");
      } catch (SQLException e) {
        log.error("Failed to add a NULL employee", e);
      }
    }

    // Initialize the service requests table if not initialized
    if (isNotInitialized(Table.SERVICE_REQUESTS_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String queryTable = "CREATE TABLE " + Table.SERVICE_REQUESTS_TABLE
            + "(" + ServiceRequestProperty.REQUEST_ID.getColumnName() + " LONG VARCHAR, "
            + ServiceRequestProperty.REQUEST_TIME.getColumnName() + " LONG VARCHAR, "
            + ServiceRequestProperty.REQUEST_NODE.getColumnName() + " LONG VARCHAR REFERENCES "
            + Table.NODES_TABLE + "(" + NodeProperty.NODE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + ServiceRequestProperty.TYPE.getColumnName() + " LONG VARCHAR, "
            + ServiceRequestProperty.STATUS.getColumnName() + " LONG VARCHAR, "
            + ServiceRequestProperty.REQUESTER_NAME.getColumnName() + " LONG VARCHAR, "
            + ServiceRequestProperty.WHO_MARKED.getColumnName() + " LONG VARCHAR REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + ServiceRequestProperty.EMPLOYEE_ASSIGNED.getColumnName() + " LONG VARCHAR REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + ServiceRequestProperty.DATA.getColumnName() + " LONG VARCHAR, "
            + "PRIMARY KEY (" + ServiceRequestProperty.REQUEST_ID.getColumnName() + "))";
        stmt.execute(queryTable);
        log.info("Table " + Table.SERVICE_REQUESTS_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.SERVICE_REQUESTS_TABLE, e);
      }
    }
  }

  /**
   * Adds the specified node to the database
   *
   * @param nodeID    the id of the node
   * @param xCoord    the x coordinate of the node
   * @param yCoord    the y coordinate of the node
   * @param floor     the floor of the building that the node lies on
   * @param building  the building the node is in
   * @param nodeType  the type of the node
   * @param longName  the long name of the node
   * @param shortName the short name of the node
   * @return the number of affected entries
   */
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

  /**
   * Adds the specified edge to the database
   *
   * @param edgeID      the id of the edge
   * @param startNodeID the id of the start node
   * @param stopNodeID  the id of the stop node
   * @return the number of affected entries
   */
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

  /**
   * Adds the specified service request to the database
   *
   * @param requestTime   the time of the request as a string
   * @param requestNode   the id of the node where the request is going
   * @param type          the type of service request
   * @param requesterName the name of the person filling out the request
   * @param data          the data for the specific type of request
   * @return the ID of the request or "Error" if request failed to add
   */
  @Override
  public String addServiceRequest(String requestTime, String requestNode, String type,
      String requesterName, ServiceRequestData data) {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    val length = 8;
    val randomID = new StringBuilder(length);
    val rand = new Random();
    for (int i = 0; i < length; ++i) {
      val index = rand.nextInt(chars.length());
      randomID.append(chars.charAt(index));
    }
    val query = "INSERT into " + Table.SERVICE_REQUESTS_TABLE
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, randomID.toString());
      stmt.setString(2, requestTime);
      stmt.setString(3, requestNode);
      stmt.setString(4, type);
      stmt.setString(5, "Unassigned");
      stmt.setString(6, requesterName);
      stmt.setString(7, "0");
      stmt.setString(8, "0");
      stmt.setString(9, new Gson().toJson(data));
      stmt.executeUpdate();
      log.info("Added service request with ID " + randomID.toString());
      return randomID.toString();
    } catch (SQLException e) {
      log.error("Failed to add a new service request with ID " + randomID.toString(), e);
      return null;
    }
  }

  /**
   * Adds the specified employee to the database
   *
   * @param employeeID  the id of the employee
   * @param name        the name of the employee
   * @param type        the type of employee they are
   * @param isAvailable true if available, false if not available
   * @return the number of affected entries
   */
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
    //ADD CHECK FOR EMPLOYEES
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
   * @param table           the table to perform the update on
   * @param property        the property (column) for the update
   * @param id              the id of the record to update
   * @param newInfoProperty the property (column) for the new info
   * @param data            the new data for the specified property
   * @return the number of affected entries
   */
  @Override
  public int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
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

  /**
   * Updates an int in a record of the specified column of the specified table
   *
   * @param table           the table to perform the update on
   * @param property        the property (column) for the update
   * @param id              the id of the record to update
   * @param newInfoProperty the property (column) for the new info
   * @param data            the new data for the specified property
   * @return the number of affected entries
   */
  @Override
  public int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
      int data) {
    // todo GREG-play around with removing this (and just cast int to string) to remove this method
    val query = "UPDATE " + table.getTableName() + " set "
        + newInfoProperty.getColumnName() + " = ? "
        + "WHERE " + property.getColumnName() + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, data);
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

  /**
   * Gets the edges of this database. Should only be used for exporting the database, not for A*!
   *
   * @return a list of the edges this database contains
   */
  @Override
  public List<Edge> exportEdges() {
    val edges = new LinkedList<Edge>();
    val query = "SELECT * from " + Table.EDGES_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        edges.add(new Edge(rset.getString(1),
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
    val gson = new Gson();
    val serviceRequests = new LinkedList<ServiceRequest>();
    val query = "SELECT * from " + Table.SERVICE_REQUESTS_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        ServiceRequestData data = null;
        switch (rset.getString(4)) {
          case "Sanitation":
            data = gson.fromJson(rset.getString(9), SanitationRequestData.class);
            break;
        }
        serviceRequests.add(new ServiceRequest(rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8),
            data));
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
        employees.add(new Employee(rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getBoolean(4)));
      }
    } catch (SQLException e) {
      log.error("Failed to export employees", e);
    }
    return employees;
  }

  @Override
  public String employeeNameFromID(String id) {
    val query = "SELECT " + EmployeeProperty.NAME.getColumnName() + " from " + Table.EMPLOYEE_TABLE
        .getTableName()
        + " WHERE " + EmployeeProperty.EMPLOYEE_ID.getColumnName() + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, id);
      val rset = stmt.executeQuery();
      while (rset.next()) {
        val name = rset.getString(1);
        return name;
      }
    } catch (SQLException e) {
      log.error("Failed to find name", e);
    }
    return "Failed to find name";
  }
}