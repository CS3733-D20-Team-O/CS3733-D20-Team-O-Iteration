package edu.wpi.onyx_ouroboros.model.data.database;

import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Value;
import lombok.val;

/**
 * Implements DatabaseWrapper for tests that require a DatabaseWrapper
 * <p>
 * DOES NOT FIRE THE APPLICABLE DATABASE EVENTS
 */
@Value
public class DatabaseWrapperTestImpl implements DatabaseWrapper {

  Map<String, PrototypeNode> nodeMap = new HashMap<>();

  @Override
  public void addNode(PrototypeNode node) {
    nodeMap.put(node.getNodeID(), node);
  }

  @Override
  public List<PrototypeNode> export() {
    val list = new LinkedList<PrototypeNode>();
    nodeMap.keySet().forEach((id) -> list.add(nodeMap.get(id)));
    return list;
  }

  @Override
  public void deleteNode(String nodeID) {
    nodeMap.remove(nodeID);
  }

  @Override
  public void updateNode(String nodeID, PrototypeNode node) {
    nodeMap.put(nodeID, node);
  }
}
