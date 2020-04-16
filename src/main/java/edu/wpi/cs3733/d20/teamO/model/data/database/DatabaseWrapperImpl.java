package edu.wpi.cs3733.d20.teamO.model.data.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.model.data.Node;
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
   * The table names
   */
  private static final String NODES_TABLE = "NODES_TABLE", EDGES_TABLE = "EDGES_TABLE";
  /**
   * Column names for nodes
   */
  private static final String NODE_ID = "nodeID", X_COORD = "xCoord", Y_COORD = "yCoord",
      FLOOR = "floor", BUILDING = "building", NODE_TYPE = "nodeType",
      LONG_NAME = "longName", SHORT_NAME = "shortName";
  /**
   * Column names for edges
   */
  private static final String EDGE_ID = "edgeID", START_ID = "startID", STOP_ID = "stopID";

  /**
   * The connection to use for database operations
   */
  private final Connection connection;

  @Inject
  public DatabaseWrapperImpl(Connection connection) {
    this.connection = connection;
    if (!isInitialized()) {
      init();
    }
  }

  /**
   * @return whether or not the database is initialized
   */
  private boolean isInitialized() {
    // todo update to use edge table too
    try {
      return connection.getMetaData().getTables(null, null, NODES_TABLE, null).next();
    } catch (SQLException e) {
      log.warn("SQLException thrown while checking if the database was initialized", e);
      return false;
    }
  }

  /**
   * Initializes the database
   */
  private void init() {
    // todo update to use edge table too (and use column IDs at top of file)
    try (val stmt = connection.createStatement()) {
      String query = "CREATE TABLE " + NODES_TABLE
          + "(nodeID VARCHAR(255), "
          + "xCoord INT, "
          + "yCoord INT, "
          + "floor INT, "
          + "building VARCHAR(255), "
          + "nodeType VARCHAR(255), "
          + "longName VARCHAR(255), "
          + "shortName VARCHAR(255), "
          + "PRIMARY KEY (nodeID))";
      stmt.execute(query);
      log.info("Table " + NODES_TABLE + " created");
    } catch (SQLException e) {
      log.error("Failed to initialize " + NODES_TABLE, e);
    }
  }

  private int updateGenericString(String table, String column, String id, String data) {
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

  // todo GREG - play around with removing this (and just cast int to string) to remove this method
  private int updateGenericInt(String table, String column, String id, int data) {
    // todo
    return 0;
  }


  @Override
  public int addNode(String nodeID, int xCoord, int yCoord, int floor, String building,
      String nodeType, String longName, String shortName) {
    val query = "INSERT into " + NODES_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
  public int deleteNode(String nodeID) {
    val query = "DELETE from " + NODES_TABLE + " WHERE " + NODE_ID + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, nodeID);
      val nodesAffected = stmt.executeUpdate();
      log.info("Deleted node with ID " + nodeID);
      log.debug("Result of delete node was " + nodesAffected);
      return nodesAffected;
    } catch (SQLException e) {
      log.error("Failed to delete node with ID " + nodeID, e);
      return -1;
    }
  }

  @Override
  public int updateNodeID(String oldID, String newID) {
    return updateGenericString(NODES_TABLE, NODE_ID, oldID, newID);
  }

  @Override
  public int updateNodeXCoord(String nodeID, int xCoord) {
    return updateGenericInt(NODES_TABLE, X_COORD, nodeID, xCoord);
  }

  @Override
  public int updateNodeYCoord(String nodeID, int yCoord) {
    return updateGenericInt(NODES_TABLE, Y_COORD, nodeID, yCoord);
  }

  @Override
  public int updateNodeFloor(String nodeID, int floor) {
    return updateGenericInt(NODES_TABLE, FLOOR, nodeID, floor);
  }

  @Override
  public int updateNodeBuilding(String nodeID, String building) {
    return updateGenericString(NODES_TABLE, BUILDING, nodeID, building);
  }

  @Override
  public int updateNodeType(String nodeID, String nodeType) {
    return updateGenericString(NODES_TABLE, NODE_TYPE, nodeID, nodeType);
  }

  @Override
  public int updateNodeLongName(String nodeID, String longName) {
    return updateGenericString(NODES_TABLE, LONG_NAME, nodeID, longName);
  }

  @Override
  public int updateNodeShortName(String nodeID, String shortName) {
    return updateGenericString(NODES_TABLE, SHORT_NAME, nodeID, shortName);
  }

  @Override
  public int addEdge(String edgeID, String startNodeID, String stopNodeID) {
    val query = "INSERT into " + EDGES_TABLE + " VALUES (?, ?, ?)";
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
  public int deleteEdge(String edgeID) {
    val query = "DELETE from " + EDGES_TABLE + " WHERE " + EDGE_ID + " = ?";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, edgeID);
      val edgesAffected = stmt.executeUpdate();
      log.info("Deleted edge with ID " + edgeID);
      log.debug("Result of delete edge was " + edgesAffected);
      return edgesAffected;
    } catch (SQLException e) {
      log.error("Failed to delete edge with ID " + edgeID, e);
      return -1;
    }
  }

  @Override
  public int updateEdgeID(String oldID, String newID) {
    return updateGenericString(EDGES_TABLE, EDGE_ID, oldID, newID);
  }

  @Override
  public int updateEdgeStart(String edgeID, String startNodeID) {
    return updateGenericString(EDGES_TABLE, START_ID, edgeID, startNodeID);
  }

  @Override
  public int updateEdgeStop(String edgeID, String stopNodeID) {
    return updateGenericString(EDGES_TABLE, STOP_ID, edgeID, stopNodeID);
  }

  /**
   * @return a map of all nodeIDs stored in this database to their corresponding Nodes
   */
  @Override
  public Map<String, Node> export() {
    val map = new HashMap<String, Node>();
    // todo pseudocode below
    // select all nodes
    // for each node in result set
    //   map.put(nodeID, node);
    // Victoria -- I did this edge parsing part for you. Just make code for the pseudocode above
    for (val edge : exportEdges()) {
      val start = map.get(edge.getStartNodeID());
      val stop = map.get(edge.getStopNodeID());
      start.getNeighbors().add(stop);
      stop.getNeighbors().add(start); // fixme remove if not supposed to be bi-directional graph
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
}