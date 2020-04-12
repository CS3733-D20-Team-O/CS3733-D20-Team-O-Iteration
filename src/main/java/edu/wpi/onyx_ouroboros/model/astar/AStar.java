package edu.wpi.onyx_ouroboros.model.astar;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

/**
 * Represents the AStar algorithm implementation and its dependencies
 */
@Value
@AllArgsConstructor(onConstructor_ = {@Inject})
public class AStar {

  /**
   * The NodeMap to use in the search
   */
  NodeMap nodeMap;

  /**
   * Finds the distance between two nodes using the Pythagorean theorem
   *
   * @param n1 the first node
   * @param n2 the second node
   * @return the Euclidean distance between both nodes
   */
  public static double distanceBetween(Node n1, Node n2) {
    val dX = n1.getXCoord() - n2.getXCoord();
    val dY = n1.getYCoord() - n2.getYCoord();
    return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
  }

  /**
   * Finds the shortest path between a start and a stop node, if one exists
   *
   * @param start the node to start the search from
   * @param stop  the node to find a path to
   * @return the shortest path between two nodes, or null if one does not exist
   */
  public List<Node> findPathBetween(Node start, Node stop) {
    val map = nodeMap.getMap();
    // make sure to use a PriorityQueue of NodeWithPriorities (see class below)!
    // all nodes you have access to are in the map variable defined at the start of the method

    //+++++++++++++
    //this part of the function is where the searching takes place
    //+++++++++++++

    //set up the frontier, the PriorityQueue of all points to search
    //frontier uses objects of NodeWithPriority so that the nodes are able to track and record
    //their weight to comply with java's PriorityQueue
    PriorityQueue<NodeWithPriority> frontier = new PriorityQueue<>();
    frontier.add(new NodeWithPriority(start, 0));

    //this is the A* "log", it tracks for every point, one step back in the path
    //so once the pathfinding is complete, you can trace back through cameFrom from stop to get
    //the path
    HashMap<Node, Node> cameFrom = new HashMap<>();
    cameFrom.put(start, start);

    //this keeps track of the cost to get to any point, it is used to compare various paths to find
    //the cheapest path, and thus the shortest path
    HashMap<Node, Double> costSoFar = new HashMap<>();
    costSoFar.put(start, 0.0);

    while( frontier.size() != 0){

      Node current = frontier.poll().getNode(); //get first node

      if(current.equals(stop)){ //if the destination is reached, stop the search
        break;
      }

      List<Node> neighbors = current.getNeighbors(); //get every point we can reach

      for(Node n : neighbors){ //for every point we can reach

        double newCost = costSoFar.get(current) + distanceBetween(current, n); //it takes this long to get to the new node

        //if n hasn't been explored or it is cheaper to get to n this way
        if( !costSoFar.containsKey(n) || newCost < costSoFar.get(n)){

          costSoFar.put(n, newCost); //it took this long to get here
          frontier.add(new NodeWithPriority(n, newCost + distanceBetween(n, stop))); //add the distance between n and the stop node to prioritize closer nodes
          cameFrom.put(n, current); //we went through current to get here

        }
      }
    }

    //++++++++++++
    //now the searching is done, the HashMap cameFrom will contain this.stop, so all that
    //is left to do is trace back through the HashMap to get the path
    //++++++++++++

    List<Node> pathOfNodes = new LinkedList<>();
    Node backTrace = stop;

    while(!backTrace.equals(start)){ //while we haven't hit the start
      pathOfNodes.add(0, backTrace);
      backTrace = cameFrom.get(backTrace);
    }
    pathOfNodes.add(0, backTrace);

    return pathOfNodes;
  }

  /**
   * Represents a node with priority for use in the priority queue
   */
  @Value
  private static class NodeWithPriority implements Comparable<NodeWithPriority> {

    Node node;
    double priority;

    @Override
    public int compareTo(NodeWithPriority other) {
      return Double.compare(priority, other.priority);
    }
  }
}
