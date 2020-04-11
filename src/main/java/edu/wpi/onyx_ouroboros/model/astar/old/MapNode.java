package edu.wpi.onyx_ouroboros.model.astar.old;

import lombok.Value;

@Value
public class MapNode {

  String ID;
  int x;
  int y;

  /**
   * gets the pythagorean distance to a point
   *
   * @param that the MapNode to calculate the distance to
   * @return distance to that
   */
  public int distanceTo(MapNode that) {
    int a = Math.abs(this.x - that.getX());
    int b = Math.abs(this.y - that.getY());
    a = a * a;
    b = b * b;
    double c = Math.sqrt(a + b);
    return (int) c;
  }
}
