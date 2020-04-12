package edu.wpi.onyx_ouroboros.model.astar;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Value;
import lombok.val;

@Value
public class NodeMap {

  /**
   * The node map of node IDs to the nodes
   */
  Map<String, Node> map = new HashMap<>();

  /**
   * Creates a NodeMap from the given database
   *
   * @param database the database to load the map from
   */
  @Inject
  public NodeMap(DatabaseWrapper database) {
    database.export().forEach((dbNode) -> {
      val node = new NodeImpl(
          dbNode.getNodeID(),
          dbNode.getXCoord(),
          dbNode.getYCoord(),
          new LinkedList<>()
      );
      map.put(node.getID(), node);
    });
//    database.exportEdges().forEach((dbEdge) -> {
//      val start = map.get(dbEdge.getStartID());
//      val stop = map.get(dbEdge.getStopID());
//      start.getNeighbors().add(stop);
//      // if bidirectional add stop.getNeighbors().add(start);
//    });
  }

  /**
   * Implements the Node functionality required by A*
   */
  @Value
  private static class NodeImpl implements Node {

    String ID;
    int xCoord, yCoord;
    List<Node> neighbors;
  }
}
