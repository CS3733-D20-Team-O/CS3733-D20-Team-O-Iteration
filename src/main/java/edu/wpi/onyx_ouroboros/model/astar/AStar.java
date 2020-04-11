package edu.wpi.onyx_ouroboros.model.astar;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

/**
 * Represents the AStar algorithm implementation and its dependencies
 */
@Value
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AStar {

  /**
   * The NodeMap to use in the search
   */
  NodeMap nodeMap;

  /**
   * Finds the distance between two nodes using the Pythagorean theorem
   *
   * @param n1 the first node
   * @param n2 the second node
   * @return the Euclidean distance between both nodes
   */
  public double distanceBetween(PrototypeNode n1, PrototypeNode n2) {
    val dX = n1.getXCoord() - n2.getXCoord();
    val dY = n1.getYCoord() - n2.getYCoord();
    return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
  }

  /**
   * Finds the shortest path between a start and a stop node, if one exists
   *
   * @param start the node to start the search from
   * @param stop  the node to find a path to
   * @return the shortest path between two nodes, or null if one does not exist
   */
  public Object findPath(PrototypeNode start, PrototypeNode stop) {
    // todo create all a-star specific runtime objects here
    //   and make return type Path, not Object
    // make sure to use a priority queue of NodeWithPriorities!
    return null;
  }

  @Value
  private static class NodeWithPriority implements Comparable<NodeWithPriority> {

    PrototypeNode node;
    double priority;

    // todo i am not sure if this is correct. You might need to swap the order
    @Override
    public int compareTo(NodeWithPriority other) {
      return Double.compare(priority, other.priority);
    }
  }
}
