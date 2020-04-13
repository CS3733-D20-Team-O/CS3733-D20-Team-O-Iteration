package edu.wpi.onyx_ouroboros.model.astar;

import java.util.LinkedList;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
class NodeTestImp implements Node{

  private final String ID;
  private final int XCoord;
  private final int YCoord;
  private final LinkedList<Node> neighbors;


}
