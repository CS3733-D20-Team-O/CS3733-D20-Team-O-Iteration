package edu.wpi.onyx_ouroboros.model.astar;

import com.google.inject.Inject;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Value;

@Value
public class NodeMap {

  Map<String, Node> map = new HashMap<>();

  @Inject
  public NodeMap(DatabaseWrapper database) {
    // todo load nodeMap from database by merging list of nodes and list of edges
  }
}
