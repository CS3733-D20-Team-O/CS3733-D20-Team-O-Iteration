package edu.wpi.onyx_ouroboros.model.astar;

import java.util.LinkedList;
import lombok.Value;

@Value
class NodeTestImp implements Node{

  String ID;
  int XCoord;
  int YCoord;
  LinkedList<Node> neighbors;


}
