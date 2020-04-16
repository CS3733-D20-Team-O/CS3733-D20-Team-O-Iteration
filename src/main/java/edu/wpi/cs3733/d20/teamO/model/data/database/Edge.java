package edu.wpi.cs3733.d20.teamO.model.data.database;

import lombok.Value;

/**
 * Represents an edge in the database.
 * <p>
 * Should only be used for exporting edges from the database (and not for A*)!
 */
@Value
class Edge {

  String edgeID, startNodeID, stopNodeID;
}
