package edu.wpi.onyx_ouroboros.model.data;

import java.util.LinkedList;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents a Node and its associated data
 */
@Value
@EqualsAndHashCode(exclude = "neighbors") // exclude neighbors to prevent StackOverflow
public class Node {

  String nodeID;
  int xCoord, yCoord, floor;
  String building, nodeType, longName, shortName;
  LinkedList<Node> neighbors = new LinkedList<>();
}
