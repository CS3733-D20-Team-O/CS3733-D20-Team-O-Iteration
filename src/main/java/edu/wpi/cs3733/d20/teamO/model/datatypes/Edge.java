package edu.wpi.cs3733.d20.teamO.model.datatypes;

import lombok.Value;

/**
 * Represents an edge in the database
 */
@Value
public class Edge {

  String edgeID, startNodeID, stopNodeID;
}
