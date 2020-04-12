package edu.wpi.onyx_ouroboros.model.data.database;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

/**
 * The default implementation for a DatabaseWrapper
 */
@Slf4j
class DatabaseWrapperImpl implements DatabaseWrapper {

  /**
   * The PrototypeNode table name
   */
  private static final String TABLE_NAME = "PROTOTYPE_TABLE";

  /**
   * The connection to use for database operations
   */
  private final Connection connection;

  /**
   * The EventBus to dispatch DatabaseEvents to
   */
  private final EventBus eventBus;

  /**
   * Constructs a DatabaseWrapperImpl
   *
   * @param connection the database connection to use
   * @param eventBus   the event bus to post events to
   */
  @Inject
  public DatabaseWrapperImpl(Connection connection, EventBus eventBus) {
    this.connection = connection;
    this.eventBus = eventBus;
    if (!isInitialized()) {
      init();
    }
  }

  /**
   * @return whether or not the database is initialized
   */
  private boolean isInitialized() {
    try {
      return connection.getMetaData().getTables(null, null, TABLE_NAME, null).next();
    } catch (SQLException e) {
      log.warn("SQLException thrown while checking if the database was initialized", e);
      return false;
    }
  }

  /**
   * Initializes the database if not already initialized
   */
  private void init() {
    try (val stmt = connection.createStatement()) {
      String query = "CREATE TABLE " + TABLE_NAME
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
      log.info("Table " + TABLE_NAME + " created");
    } catch (SQLException e) {
      log.error("Failed to initialize " + TABLE_NAME, e);
    }
  }

  /**
   * Adds the given PrototypeNode to the database
   * <p>
   * Broadcasts DatabaseNodeInsertedEvent upon successful addition
   *
   * @param node the node to add
   */
  @Override
  public void addNode(PrototypeNode node) {
    val query = "INSERT into " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (val stmt = connection.prepareStatement(query)) {
      stmt.setString(1, node.getNodeID());
      stmt.setInt(2, node.getXCoord());
      stmt.setInt(3, node.getYCoord());
      stmt.setInt(4, node.getFloor());
      stmt.setString(5, node.getBuilding());
      stmt.setString(6, node.getNodeType());
      stmt.setString(7, node.getLongName());
      stmt.setString(8, node.getShortName());
      stmt.executeUpdate();
      log.info("Added a new node with ID " + node.getNodeID());
    } catch (SQLException e) {
      log.error("Failed to add a new node with ID " + node.getNodeID(), e);
    }
  }

  /**
   * Exports all records of the database as PrototypeNodes
   *
   * @return a list of PrototypeNodes
   */
  @Override
  public List<PrototypeNode> export() {
    return null; // todo
  }

  /**
   * Deletes the specified PrototypeNode from the database using its ID
   * <p>
   * Broadcasts DatabaseNodeDeletedEvent upon successful removal
   *
   * @param nodeID the nodeID of the PrototypeNode to remove from the database
   */
  @Override
  public void deleteNode(String nodeID) {
    // todo
  }

  /**
   * Updates a given PrototypeNode in the database
   * <p>
   * Broadcasts DatabaseNodeUpdatedEvent upon successful update
   *
   * @param nodeID the ID of the PrototypeNode to update
   * @param node   the new data for the PrototypeNode
   */
  @Override
  public void updateNode(String nodeID, PrototypeNode node) {
    // todo
  }

  /**
   * Closes any database wrapper specific properties but NOT the connection
   *
   * @throws Exception in case of an exception
   */
  @Override
  public void close() throws Exception {
    // todo if nothing to close here, let Greg know
    //   because then DatabaseWrapper will not need to extend AutoClosable
  }
}
