package edu.wpi.cs3733.d20.teamO.model.path_finding;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import lombok.val;

class BFS implements PathFinder {

  /**
   * Finds the path (if one exists) between the start and stop nodes
   *
   * @param start the node to start the search at
   * @param stop  the node to stop the search at
   * @return the path of nodes taken to get to the stop, or null if no path was found
   */
  @Override
  public List<Node> findPathBetween(Node start, Node stop) {
    // Set up the frontier, the Queue of all points to search.

    val frontier = new LinkedList<Node>();
    frontier.add(start);

    // Once the path finding is complete, you trace back through cameFrom from stop to get the path
    val cameFrom = new HashMap<Node, Node>();
    cameFrom.put(start, start);

    // This is where the actual searching begins
    // The while loop runs until all points are searched or stop is found
    while (!frontier.isEmpty()) {
      // Get first node
      val current = frontier.poll();

      // If the destination is reached, stop the search
      if (current.equals(stop)) {
        break;
      }

      // Iterate through the current node's neighbors
      for (val n : current.getNeighbors()) {
        // It takes this long to get to the new node
        if (!cameFrom.containsKey(n)) {
          frontier.add(n);
          // We went through current to get here
          cameFrom.put(n, current);
        }
      }
    }

    // If stop couldn't be reached
    if (!cameFrom.containsKey(stop)) {
      return null;
    }

    // Now the searching is done, the HashMap cameFrom will contain this.stop, so all that
    //  is left to do is trace back through the HashMap to get the path
    val pathOfNodes = new LinkedList<Node>();

    // Until we hit the start Node
    for (Node backTrace = stop; !backTrace.equals(start); backTrace = cameFrom.get(backTrace)) {
      pathOfNodes.push(backTrace);
    }
    pathOfNodes.push(start);
    return pathOfNodes;
  }
}

