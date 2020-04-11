package edu.wpi.onyx_ouroboros.model.astar.old;

public class Main {

  public static void main(String[] args) {
    MapSearcher pathFinder = new MapSearcher();
    Parser parser = new Parser();
    parser.create();

    pathFinder.setStart(parser.getNode("a"));
    pathFinder.setStop(parser.getNode("e"));

    Path path = null;
    path = pathFinder.AStar();
    path.printPath();
  }
}