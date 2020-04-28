package edu.wpi.cs3733.d20.teamO.model.path_finding;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.List;

/**
 * Represents what a path finding algorithm must be capable of doing
 */
public interface PathFinder {

  /**
   * Finds the path (if one exists) between the start and stop nodes
   *
   * @param start the node to start the search at
   * @param stop  the node to stop the search at
   * @return the path of nodes taken to get to the stop, or null if no path was found
   */
  List<Node> findPathBetween(Node start, Node stop);
}
