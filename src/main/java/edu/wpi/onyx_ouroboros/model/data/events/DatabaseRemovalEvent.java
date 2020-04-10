package edu.wpi.onyx_ouroboros.model.data.events;

import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import lombok.Value;

/**
 * An event for when a record is removed from a database
 */
@Value
public class DatabaseRemovalEvent implements DatabaseEvent {

  /**
   * The node removed from the database
   */
  PrototypeNode node;
}
