package edu.wpi.cs3733.d20.teamO.model.database;

import com.google.inject.ImplementedBy;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.TableProperty;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Edge;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.util.List;
import java.util.Map;

/**
 * Interface that represents what a database can do
 */
@ImplementedBy(value = DatabaseWrapperImpl.class)
public interface DatabaseWrapper {

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
   * Adds the specified edge to the database
   *
   * @param edgeID      the id of the edge
   * @param startNodeID the id of the start node
   * @param stopNodeID  the id of the stop node
   * @return the number of affected entries
   */
  int addEdge(String edgeID, String startNodeID, String stopNodeID);

  /**
   * Adds the specified service request to the database
   *
   * @param requestID        the id of the request
   * @param requestTime      the time of the request as a string
   * @param requestNode      the id of the node where the request is going
   * @param type             the type of service request
   * @param requesterName    the name of the person filling out the request
   * @param whoMarked        the id of the admin (employee) who assigns the request
   * @param employeeAssigned the id of the employee assigned to fulfill the request
   * @return the number of affected entries
   */
  int addServiceRequest(String requestID, String requestTime, String requestNode, String type,
      String requesterName, String whoMarked, String employeeAssigned);

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
   * Deletes record(s) matching the specified criteria (example: an edge based on its ID)
   *
   * @param matchingProperty the property to use for deletion (typically should be ID)
   * @param matching         what to delete from the database (typically should be ID)
   * @return the number of affected entries
   */
  int deleteMatching(TableProperty matchingProperty, String matching);

  /**
   * Updates a String in a record of the specified column of the specified table
   * <p>
   * matching fields are used to select which records to update
   * <p>
   * Both matchingProperty AND dataProperty MUST BELONG TO THE SAME TABLE!
   *
   * @param matchingProperty the property (column) to use for matching in order to update
   * @param matching         the string to use for matching in order to update
   * @param dataProperty     the property (column) of the new data (where the data should be saved)
   * @param data             the new data for the specified property
   * @return the number of affected entries
   */
  int update(TableProperty matchingProperty, String matching,
      TableProperty dataProperty, String data);

  /**
   * @return a map of all nodeIDs stored in this database to their corresponding Nodes
   */
  Map<String, Node> exportNodes();

  /**
   * @return a list of the edges this database contains
   */
  List<Edge> exportEdges();

  /**
   * @return a list of the service requests stored in this database
   */
  List<ServiceRequest> exportServiceRequests();

  /**
   * @return a list of the employees stored in this database
   */
  List<Employee> exportEmployees();
}
