package edu.wpi.onyx_ouroboros.model.astar.old;

import java.util.LinkedList;
import lombok.Value;

@Value
public class Path {

  LinkedList<MapNode> nodes;
  LinkedList<MapEdge> edges;

  public void printPath() {
    for (MapNode n : nodes) {
      System.out.println(n.getID());
    }
  }

  public LinkedList<MapNode> getNodes () {
    return nodes;
  }
}
