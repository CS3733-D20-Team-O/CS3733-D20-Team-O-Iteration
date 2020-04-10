package edu.wpi.onyx_ouroboros.model.data.events;

import lombok.Value;

/**
 * An event for when a record is removed from a database
 */
@Value
public class DatabaseNodeDeletedEvent implements DatabaseEvent {

  /**
   * The ID of the node removed from the database
   */
  String nodeID;
}
