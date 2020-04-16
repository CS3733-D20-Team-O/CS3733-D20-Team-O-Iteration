package edu.wpi.cs3733.d20.teamO.model.data.database;

import com.google.inject.ImplementedBy;
import edu.wpi.cs3733.d20.teamO.model.data.Node;
import java.util.Map;

/**
 * Interface that represents what a wrapper for a database that stores Nodes can do
 * <p>
 * Return values are the number of affected entries unless specified otherwise
 */
@ImplementedBy(value = DatabaseWrapperImpl.class)
public interface DatabaseWrapper {

  int addNode(String nodeID, int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName);

  int deleteNode(String nodeID);

  int updateNodeID(String oldID, String newID);

  int updateNodeXCoord(String nodeID, int xCoord);

  int updateNodeYCoord(String nodeID, int yCoord);

  int updateNodeFloor(String nodeID, int floor);

  int updateNodeBuilding(String nodeID, String building);

  int updateNodeType(String nodeID, String nodeType);

  int updateNodeLongName(String nodeID, String longName);

  int updateNodeShortName(String nodeID, String shortName);

  int addEdge(String edgeID, String startNodeID, String stopNodeID);

  int deleteEdge(String edgeID);

  int updateEdgeID(String oldID, String newID);

  int updateEdgeStart(String edgeID, String startNodeID);

  int updateEdgeStop(String edgeID, String stopNodeID);

  /**
   * @return a map of all nodeIDs stored in this database to their corresponding Nodes
   */
  Map<String, Node> export();
}
