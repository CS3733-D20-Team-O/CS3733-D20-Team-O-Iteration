package edu.wpi.onyx_ouroboros.model.data.database;


import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.sql.Connection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.greenrobot.eventbus.EventBus;

/**
 * The default implementation for a DatabaseWrapper
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class DatabaseWrapperImpl implements DatabaseWrapper {

  /**
   * The connection to use for database operations
   */
  private final Connection connection;

  /**
   * The EventBus to dispatch DatabaseEvents to
   */
  private final EventBus eventBus;

  /**
   * Adds the given PrototypeNode to the database
   * <p>
   * Broadcasts DatabaseNodeInsertedEvent upon successful addition
   *
   * @param node the node to add
   */
  @Override
  public void addNode(PrototypeNode node) {
    // todo
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
