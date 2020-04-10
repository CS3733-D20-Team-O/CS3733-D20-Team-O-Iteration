package edu.wpi.onyx_ouroboros.model.data;

import java.util.List;

/**
 * Interface that represents a wrapper for a database that stores PrototypeNodes
 */
public interface IDatabaseWrapper extends AutoCloseable {

  /**
   * Adds the given PrototypeNode to the database
   * <p>
   * Broadcasts DatabaseNodeInsertedEvent upon successful addition
   *
   * @param node the node to add
   */
  void addNode(PrototypeNode node);

  /**
   * Exports all records of the database as PrototypeNodes
   *
   * @return a list of PrototypeNodes
   */
  List<PrototypeNode> export();

  /**
   * Deletes the specified PrototypeNode from the database using its ID
   * <p>
   * Broadcasts DatabaseNodeDeletedEvent upon successful removal
   *
   * @param nodeID the nodeID of the PrototypeNode to remove from the database
   */
  void deleteNode(String nodeID);

  /**
   * Updates a given PrototypeNode in the database
   * <p>
   * Broadcasts DatabaseNodeUpdatedEvent upon successful update
   *
   * @param nodeID the ID of the PrototypeNode to update
   * @param node   the new data for the PrototypeNode
   */
  void updateNode(String nodeID, PrototypeNode node);
}
