package edu.wpi.onyx_ouroboros.model.astar;

import java.util.List;

/**
 * Represents what an A* capable node must be able to do
 */
public interface Node {

  /**
   * @return the ID of this node
   */
  String getID();

  /**
   * @return the x coordinate of this node
   */
  int getXCoord();

  /**
   * @return the y coordinate of this node
   */
  int getYCoord();

  /**
   * @return the neighbors of this node
   */
  List<Node> getNeighbors();
}
