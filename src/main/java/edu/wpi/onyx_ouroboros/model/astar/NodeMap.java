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

  Map<String, Node> map = new HashMap<>();

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
    database.exportEdges().forEach((dbEdge) -> {

    });
    // todo load nodeMap from database by merging list of nodes and list of edges
  }

  @Value
  private static class NodeImpl implements Node {

    String ID;
    int xCoord, yCoord;
    List<Node> neighbors;
  }
}
