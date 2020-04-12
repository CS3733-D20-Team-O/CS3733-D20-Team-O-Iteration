package edu.wpi.onyx_ouroboros.model.astar;

import java.util.LinkedList;

public class NodeTestImplementation implements Node{

  String ID;
  int x;
  int y;
  LinkedList<Node> neighbors;

  public NodeTestImplementation(String ID, int x, int y){
    this.ID = ID;
    this.x = x;
    this.y = y;
    this.neighbors = new LinkedList<>();
  }

  public void setNeighbors(LinkedList<Node> neighbors) {
    this.neighbors = neighbors;
  }

  @Override
  public LinkedList<Node> getNeighbors() {
    return neighbors;
  }

  @Override
  public int getXCoord() {
    return x;
  }

  @Override
  public int getYCoord() {
    return y;
  }

  @Override
  public String getID() {
    return ID;
  }
}
