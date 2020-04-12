package edu.wpi.onyx_ouroboros.model.astar;

import com.google.inject.Inject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

/**
 * Represents the AStar algorithm implementation and its dependencies
 */
@Value
@AllArgsConstructor(onConstructor_ = {@Inject})
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
  public static double distanceBetween(Node n1, Node n2) {
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
  public List<Node> findPathBetween(Node start, Node stop) {
    val map = nodeMap.getMap();
    // todo create all A* specific objects here, such as the different cost maps
    // make sure to use a PriorityQueue of NodeWithPriorities (see class below)!
    // all nodes you have access to are in the map variable defined at the start of the method
    return null;
  }

  /**
   * Represents a node with priority for use in the priority queue
   */
  @Value
  private static class NodeWithPriority implements Comparable<NodeWithPriority> {

    Node node;
    double priority;

    @Override
    public int compareTo(NodeWithPriority other) {
      // todo I am not sure if this is correct. You might need to swap the order
      return Double.compare(priority, other.priority);
    }
  }
}
