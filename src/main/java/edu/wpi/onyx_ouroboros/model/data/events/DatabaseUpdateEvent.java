package edu.wpi.onyx_ouroboros.model.data.events;

import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import lombok.Value;

/**
 * An event for when a record in a database is updated
 */
@Value
public class DatabaseUpdateEvent implements DatabaseEvent {

  /**
   * The old ID of the node that was updated
   */
  String nodeID;
  /**
   * The updated PrototypeNode in the database
   */
  PrototypeNode node;
}
