package edu.wpi.onyx_ouroboros.model.data;

import lombok.Value;

/**
 * Represents a record in PrototypeNodes.csv
 */
@Value
public class PrototypeNode {

  String nodeID;
  int xCoord, yCoord, floor;
  String building, nodeType, longName, shortName;
}
