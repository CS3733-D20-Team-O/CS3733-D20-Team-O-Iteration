package edu.wpi.cs3733.d20.teamO.model.data;

import com.google.gson.Gson;
import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.EdgeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.NodeProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.SchedulerProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.data.db_model.TableProperty;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Scheduler;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.AVRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ExternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.FloristDeliveryData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.GiftDeliveryRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InfoTechRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InterpreterData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.MedicineDeliveryServiceData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
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
    if (init()) {
      val csvHandler = new CSVHandlerImpl(this);
      csvHandler.importNodes("src/main/resources/CSV/MapOAllnodes (final).csv");
      csvHandler.importEdges("resources/CSV/MapOAlledges (final).csv");
      csvHandler.importEmployees("CSV/demoEmployees.csv");
    }
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
  private boolean init() {
    boolean init = false;
    // Initialize the nodes table if not initialized
    if (isNotInitialized(Table.NODES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.NODES_TABLE
            + "(" + NodeProperty.NODE_ID.getColumnName() + " VARCHAR(999), "
            + NodeProperty.X_COORD.getColumnName() + " INT, "
            + NodeProperty.Y_COORD.getColumnName() + " INT, "
            + NodeProperty.FLOOR.getColumnName() + " VARCHAR(999), "
            + NodeProperty.BUILDING.getColumnName() + " VARCHAR(999), "
            + NodeProperty.NODE_TYPE.getColumnName() + " VARCHAR(999), "
            + NodeProperty.LONG_NAME.getColumnName() + " VARCHAR(999), "
            + NodeProperty.SHORT_NAME.getColumnName() + " VARCHAR(999), "
            + "PRIMARY KEY (" + NodeProperty.NODE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.NODES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.NODES_TABLE, e);
      }
      init = true;
    }

    // Initialize the edges table if not initialized
    if (isNotInitialized(Table.EDGES_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EDGES_TABLE
            + "(" + EdgeProperty.EDGE_ID.getColumnName() + " VARCHAR(999), "
            + EdgeProperty.START_ID.getColumnName() + " VARCHAR(999) REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + EdgeProperty.STOP_ID.getColumnName() + " VARCHAR(999) REFERENCES "
            + Table.NODES_TABLE + " (" + NodeProperty.NODE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + "PRIMARY KEY (" + EdgeProperty.EDGE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.EDGES_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EDGES_TABLE, e);
      }
      init = true;
    }

    // Initialize the employee table if not initialized
    if (isNotInitialized(Table.EMPLOYEE_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.EMPLOYEE_TABLE
            + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + " VARCHAR(999), "
            + EmployeeProperty.NAME.getColumnName() + " VARCHAR(999), "
            + EmployeeProperty.TYPE.getColumnName() + " VARCHAR(999), "
            + EmployeeProperty.PASSWORD.getColumnName() + " VARCHAR(999), "
            + EmployeeProperty.IS_AVAILABLE.getColumnName() + " BOOLEAN, "
            + "PRIMARY KEY (" + EmployeeProperty.EMPLOYEE_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.EMPLOYEE_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.EMPLOYEE_TABLE, e);
      }
      if (addEmployee("0", "", "", "", false) != 1) {
        log.error("Failed to add the null employee!");
      }
      if (addEmployee("admin", "admin", "admin", "admin", false) != 1) {
        log.error("Failed to add the admin employee!");
      }
      if (addEmployee("staff", "staff", "staff", "staff", false) != 1) {
        log.error("Failed to add the staff employee!");
      }
      init = true;
    }

    // Initialize the service requests table if not initialized
    if (isNotInitialized(Table.SERVICE_REQUESTS_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String queryTable = "CREATE TABLE " + Table.SERVICE_REQUESTS_TABLE
            + "(" + ServiceRequestProperty.REQUEST_ID.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.REQUEST_TIME.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.REQUEST_NODE.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.TYPE.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.STATUS.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.REQUESTER_NAME.getColumnName() + " VARCHAR(999), "
            + ServiceRequestProperty.WHO_MARKED.getColumnName() + " VARCHAR(999) REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + ServiceRequestProperty.EMPLOYEE_ASSIGNED.getColumnName() + " VARCHAR(999) REFERENCES "
            + Table.EMPLOYEE_TABLE + "(" + EmployeeProperty.EMPLOYEE_ID.getColumnName()
            + ") ON DELETE CASCADE, "
            + ServiceRequestProperty.DATA.getColumnName() + " LONG VARCHAR, "
            + "PRIMARY KEY (" + ServiceRequestProperty.REQUEST_ID.getColumnName() + "))";
        stmt.execute(queryTable);
        log.info("Table " + Table.SERVICE_REQUESTS_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.SERVICE_REQUESTS_TABLE, e);
      }
      init = true;
    }

    // Initialize the scheduler table if not initialized
    if (isNotInitialized(Table.SCHEDULER_TABLE)) {
      try (val stmt = connection.createStatement()) {
        String query = "CREATE TABLE " + Table.SCHEDULER_TABLE
            + "(" + SchedulerProperty.SCHEDULER_ID.getColumnName() + " VARCHAR(999), "
            + SchedulerProperty.EMPLOYEE_ID.getColumnName() + " VARCHAR(999), "
            + SchedulerProperty.START_TIME.getColumnName() + " VARCHAR(999), "
            + SchedulerProperty.END_TIME.getColumnName() + " VARCHAR(999), "
            + SchedulerProperty.ROOM_TYPE.getColumnName() + " VARCHAR(999), "
            + "PRIMARY KEY (" + SchedulerProperty.SCHEDULER_ID.getColumnName() + "))";
        stmt.execute(query);
        log.info("Table " + Table.SCHEDULER_TABLE + " created");
      } catch (SQLException e) {
        log.error("Failed to initialize " + Table.SCHEDULER_TABLE, e);
      }
      init = true;
    }
    return init;
  }

  public String getNodeID(String type, String floor) {
    val takenIDs = exportNodes().keySet().stream()
        .filter(id -> id.substring(1, 5).equals(type))
        .filter(id -> id.substring(8).equals(floor))
        .map(id -> (id.substring(5, 8)))
        .map(id -> Integer.parseInt(id))
        .collect(Collectors.toSet());
    for (int i = 1; i < 1000; ++i) {
      if (!takenIDs.contains(i)) {
        return String.format("O%s%3s%2s", type, i, floor).replace(' ', '0');
      }
    }
    return null;
  }


  @Override
  public String addNode(int xCoord, int yCoord, String floor, String building,
      String nodeType, String longName, String shortName) {
    val newFloor = String.format("%2s", floor).replace(' ', '0');
    String id = getNodeID(nodeType, newFloor);
    val numAffected = addNode(id, xCoord, yCoord, floor, building, nodeType, longName, shortName);
    return (numAffected == 1) ? id : null;
  }


  @Override
  public int addNode(String nodeID, int xCoord, int yCoord, String floor, String building,
      String nodeType, String longName, String shortName) {
    val query = "INSERT into " + Table.NODES_TABLE.getTableName() +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, nodeID);
      stmt.setInt(2, xCoord);
      stmt.setInt(3, yCoord);
      stmt.setString(4, floor);
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
  public String addEdge(String startNodeID, String stopNodeID) {
    val id = startNodeID + "_" + stopNodeID;
    val numAffected = addEdge(id, startNodeID, stopNodeID);
    return (numAffected == 1) ? id : null;
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
  public String addServiceRequest(String requestTime, String location, String type,
      String requesterName, ServiceRequestData data) {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    val length = 8;
    val randomID = new StringBuilder(length);
    val rand = new Random();
    for (int i = 0; i < length; ++i) {
      val index = rand.nextInt(chars.length());
      randomID.append(chars.charAt(index));
    }
    val id = randomID.toString();
    val numAffected = addServiceRequest(id, requestTime, location, type, "Unassigned",
        requesterName, "0", "0", new Gson().toJson(data));
    return (numAffected == 1) ? id : null;
  }


  @Override
  public int addServiceRequest(String requestID, String requestTime, String location,
      String type, String status, String requesterName, String whoMarked, String employeeAssigned,
      String data) {
    val query = "INSERT into " + Table.SERVICE_REQUESTS_TABLE
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, requestID);
      stmt.setString(2, requestTime);
      stmt.setString(3, location);
      stmt.setString(4, type);
      stmt.setString(5, status);
      stmt.setString(6, requesterName);
      stmt.setString(7, whoMarked);
      stmt.setString(8, employeeAssigned);
      stmt.setString(9, data);
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
  public String addEmployee(String name, String type, String password, boolean isAvailable) {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    val length = 8;
    val randomID = new StringBuilder(length);
    val rand = new Random();
    for (int i = 0; i < length; ++i) {
      val index = rand.nextInt(chars.length());
      randomID.append(chars.charAt(index));
    }
    val id = randomID.toString();
    addEmployee(id, name, type, password, isAvailable);
    return id;
  }


  @Override
  public int addEmployee(String employeeID, String name, String type, String password,
      boolean isAvailable) {
    val query = "INSERT into " + Table.EMPLOYEE_TABLE.getTableName() + " VALUES (?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, employeeID);
      stmt.setString(2, name);
      stmt.setString(3, type);
      stmt.setString(4, password);
      stmt.setBoolean(5, isAvailable);
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
  public String addScheduler(String employeeID, String startTime, String endTime, String roomType) {
    val schedules = exportScheduler().stream()
        .map(Scheduler::getSchedulerID)
        .map(Integer::parseInt)
        .collect(Collectors.toSet());
    for (int i = 1; true; ++i) {
      if (!schedules.contains(i)) {
        val query =
            "INSERT into " + Table.SCHEDULER_TABLE.getTableName() + " VALUES (?, ?, ?, ?, ?)";
        try (val stmt = connection.prepareStatement(query)) {
          stmt.setString(1, Integer.toString(i));
          stmt.setString(2, employeeID);
          stmt.setString(3, startTime);
          stmt.setString(4, endTime);
          stmt.setString(5, roomType);
          stmt.executeUpdate();
          log.info("Added schedule with ID " + i);
        } catch (SQLException e) {
          log.error("Failed to add a new schedule with ID " + i, e);
        }
        return Integer.toString(i);
      }
    }
  }

  @Override
  public int deleteFromTable(Table table, TableProperty property, String matching) {
    // First, check to see if we are deleting from the employee table
    //  If so, we need to set corresponding service requests to the null employee
    if (Table.EMPLOYEE_TABLE.equals(table)) {
      val query = "SELECT " + EmployeeProperty.EMPLOYEE_ID.getColumnName() + " FROM "
          + Table.EMPLOYEE_TABLE + " WHERE " + property.getColumnName() + " = ?";
      try {
        val stmt = connection.prepareStatement(query);
        stmt.setString(1, matching);
        val rset = stmt.executeQuery();
        while (rset.next()) {
          val empID = rset.getString(1);
          update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.EMPLOYEE_ASSIGNED, empID,
              ServiceRequestProperty.EMPLOYEE_ASSIGNED, "0");
          update(Table.SERVICE_REQUESTS_TABLE, ServiceRequestProperty.WHO_MARKED, empID,
              ServiceRequestProperty.WHO_MARKED, "0");
        }
      } catch (SQLException e) {
        val error = "Failed to find matching record(s) from employee table using property "
            + property.getColumnName() + " and matching " + matching;
        log.error(error, e);
      }
    }

    // Execute the delete
    val query = "DELETE from " + table.getTableName() +
        " WHERE " + property.getColumnName() + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, matching);
      val affected = stmt.executeUpdate();
      log.info("Deleted " + affected + " record(s) from " + table.getTableName());
      log.debug("Result of deletion was " + affected);
      //for()
      addEmployee("0", "", "", "", false); // add null back if removed
      addEmployee("admin", "admin", "admin", "admin", false); //add admin back if removed
      addEmployee("staff", "staff", "staff", "staff", false);
      return affected;
    } catch (SQLException e) {
      val error = "Failed to delete record(s) from " + table.getTableName() +
          " using property " + property.getColumnName() + " and matching " + matching;
      log.error(error, e);
      return -1;
    }
  }


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
            rset.getString(4),
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
    val serviceRequests = new LinkedList<ServiceRequest>();
    val query = "SELECT * from " + Table.SERVICE_REQUESTS_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        Class<? extends ServiceRequestData> clazz = null;
        switch (rset.getString(4)) {
          case "Sanitation":
            clazz = SanitationRequestData.class;
            break;
          case "Interpreter":
            clazz = InterpreterData.class;
            break;
          case "Gift":
            clazz = GiftDeliveryRequestData.class;
            break;
          case "InfoTech":
            clazz = InfoTechRequestData.class;
            break;
          case "Internal Transportation":
            clazz = InternalTransportationRequestData.class;
            break;
          case "Security":
            clazz = SecurityRequestData.class;
            break;
          case "Medicine delivery":
            clazz = MedicineDeliveryServiceData.class;
            break;
          case "A/V":
            clazz = AVRequestData.class;
            break;
          case "External Transportation":
            clazz = ExternalTransportationRequestData.class;
            break;
          case "Florist":
            clazz = FloristDeliveryData.class;
            break;
          default:
            log.error("Unhandled request type");
        }
        if (clazz != null) {
          serviceRequests.add(new ServiceRequest(
              rset.getString(1),
              rset.getString(2),
              rset.getString(3),
              rset.getString(4),
              rset.getString(5),
              rset.getString(6),
              rset.getString(7),
              rset.getString(8),
              new Gson().fromJson(rset.getString(9), clazz)));
        }
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
            rset.getString(4),
            rset.getBoolean(5)));
      }
    } catch (SQLException e) {
      log.error("Failed to export employees", e);
    }
    return employees;
  }

  @Override
  public List<Scheduler> exportScheduler() {
    val schedules = new LinkedList<Scheduler>();
    val query = "SELECT * from " + Table.SCHEDULER_TABLE;
    try (val stmt = connection.prepareStatement(query); val rset = stmt.executeQuery()) {
      while (rset.next()) {
        schedules.add(new Scheduler(rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5)));
      }
    } catch (SQLException e) {
      log.error("Failed to export schedules", e);
    }
    return schedules;
  }

  @Override
  @Deprecated
  public String employeeNameFromID(String id) {
    val query = "SELECT " + EmployeeProperty.NAME.getColumnName() + " from " + Table.EMPLOYEE_TABLE
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