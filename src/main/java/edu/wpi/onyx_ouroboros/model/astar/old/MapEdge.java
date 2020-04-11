package edu.wpi.onyx_ouroboros.model.astar.old;

import lombok.Value;

@Value
public class MapEdge {

  String ID;
  MapNode start;
  MapNode stop;

  /**
   * checks if the given MapNode is the start MapNode for this edge
   *
   * @param node the node to check
   * @return true if node is equal to this.start, false otherwise
   */
  public boolean isStart(MapNode node) {
    return this.start.equals(node);
  }

  /**
   * checks if the given MapNode is the stop MapNode for this edge
   *
   * @param node the node to check
   * @return true if node is equal to this.stop, false otherwise
   */
  public boolean isStop(MapNode node) {
    return this.stop.equals(node);
  }
}
