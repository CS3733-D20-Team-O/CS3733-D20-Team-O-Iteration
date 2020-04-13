package edu.wpi.onyx_ouroboros.model.astar;

import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class NodeTestImpl implements Node {

  private final String ID;
  private final int XCoord;
  private final int YCoord;
  private final LinkedList<Node> neighbors = new LinkedList<>();
}
