package edu.wpi.cs3733.d20.teamO.model.database;

import com.google.inject.ImplementedBy;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.TableProperty;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Scheduler;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import java.util.List;
import java.util.Map;

/**
 * Interface that represents what a wrapper for a database can do
 * <p>
 * todo make better javadoc here
 */
@ImplementedBy(value = DatabaseWrapperImpl.class)
public interface DatabaseWrapper {

  /**
   * Adds the specified node to the database with auto-generated ID
   *
   * @param xCoord    the x coordinate of the node
   * @param yCoord    the y coordinate of the node
   * @param floor     the floor of the building that the node lies on
   * @param building  the building the node is in
   * @param nodeType  the type of the node
   * @param longName  the long name of the node
   * @param shortName the short name of the node
   * @return the id of the added node
   */
  String addNode(int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName);

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
  int addNode(String nodeID, int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName);

  /**
   * Adds the specified edge to the database with auto-generated ID
   *
   * @param startNodeID the id of the start node
   * @param stopNodeID  the id of the stop node
   * @return the id of the added edge
   */
  String addEdge(String startNodeID, String stopNodeID);

  /**
   * Adds the specified edge to the database
   *
   * @param edgeID      the id of the edge
   * @param startNodeID the id of the start node
   * @param stopNodeID  the id of the stop node
   * @return the number of affected entries
   */
  int addEdge(String edgeID, String startNodeID, String stopNodeID);

  /**
   * Adds the specified service request to the database with auto-generated ID
   *
   * @param requestTime   the time of the request as a string
   * @param location      the long name of the node where the request is going
   * @param type          the type of service request
   * @param requesterName the name of the person filling out the request
   * @param data          the data for the specific type of request
   * @return the ID of the request or "NULL" if request failed to add
   */
  String addServiceRequest(String requestTime, String location, String type,
      String requesterName, ServiceRequestData data);

  /**
   * Adds the specified service request to the database
   *
   * @param requestID        the id of the request
   * @param requestTime      the time of the request as a string
   * @param location         the long name of the node where the request is going
   * @param type             the type of service request
   * @param status           the status of the service request
   * @param requesterName    the name of the person filling out the request
   * @param whoMarked        the id of the admin (employee) who assigns the request
   * @param employeeAssigned the id of the employee assigned to fulfill the request
   * @param data             the data for the specific type of request
   * @return the number of affected entries
   */
  int addServiceRequest(String requestID, String requestTime, String location, String type,
      String status, String requesterName, String whoMarked, String employeeAssigned, String data);

  /**
   * Adds the specified employee to the database with auto-generated ID
   *
   * @param name        the name of the employee
   * @param type        the type of employee they are
   * @param isAvailable true if available, false if not available
   * @return the id of the added employee
   */
  String addEmployee(String name, String type, boolean isAvailable);

  /**
   * Adds the specified employee to the database
   *
   * @param employeeID  the id of the employee
   * @param name        the name of the employee
   * @param type        the type of employee they are
   * @param isAvailable true if available, false if not available
   * @return the number of affected entries
   */
  int addEmployee(String employeeID, String name, String type, boolean isAvailable);

  /**
   * Adds the specified scheduler request to the database with auto-generated ID
   *
   * @param employeeID the id of the employee
   * @param startTime  the start time for the scheduled room
   * @param endTime    the end time for the scheduled room
   * @param roomType   the type of the room scheduled
   * @return the confirmation ID
   */
  String addScheduler(String employeeID, String startTime, String endTime, String roomType);

  /**
   * Deletes record(s) (example: a node or edge) from the specified table
   *
   * @param table    the table to delete the record(s) from
   * @param property the property of the table to use for deletion (typically should be ID)
   * @param matching the string of what to delete from the database (typically should be ID)
   * @return the number of affected entries
   */
  int deleteFromTable(Table table, TableProperty property, String matching);

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
  int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
      String data);

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
  int update(Table table, TableProperty property, String id, TableProperty newInfoProperty,
      int data);

  /**
   * @return a map of all nodeIDs stored in this database to their corresponding Nodes
   */
  Map<String, Node> exportNodes();

  /**
   * Gets the edges of this database. Should only be used for exporting the database, not for A*!
   *
   * @return a list of the edges this database contains
   */
  List<Edge> exportEdges();

  /**
   * @return a list of the service requests this database contains
   */
  List<ServiceRequest> exportServiceRequests();

  /**
   * @return a list of the employees this database contains
   */
  List<Employee> exportEmployees();

  /**
   * @return a list of the scheduler information this database contains
   */
  List<Scheduler> exportScheduler();

  /**
   * @param id the id of an Employee
   * @return the name of the Employee
   */
  @Deprecated
  String employeeNameFromID(String id);
}
