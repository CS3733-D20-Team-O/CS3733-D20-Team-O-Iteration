package edu.wpi.onyx_ouroboros.events.database;

import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import lombok.Value;

/**
 * An event for when a record is added to a database
 */
@Value
public class DatabaseNodeInsertedEvent implements DatabaseEvent {

  /**
   * The node added to the database
   */
  PrototypeNode node;
}
