package edu.wpi.cs3733.d20.teamO.model.datatypes;

import java.util.LinkedList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;

/**
 * Represents a Node and its associated data
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "neighbors") // exclude neighbors to prevent StackOverflow
public class Node {

  String nodeID;
  int xCoord, yCoord;
  String floor, building, nodeType, longName, shortName;
  @With
  LinkedList<Node> neighbors;

  /**
   * Constructor to allow for neighbors to have a with call
   */
  public Node(String nodeID, int xCoord, int yCoord, String floor, String building,
      String nodeType, String longName, String shortName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    neighbors = new LinkedList<>();
  }
}
