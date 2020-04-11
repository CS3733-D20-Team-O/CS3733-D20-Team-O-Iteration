package edu.wpi.onyx_ouroboros.model.astar.old;


import java.util.HashMap;
import java.util.LinkedList;

public class MapSearcher {

    private final Parser parser = new Parser();
    private MapNode start;
    private MapNode stop;

    public MapSearcher() {
        this.start = null;
        this.stop = null;
        parser.create();
    }

    public void setStart(MapNode start) {
        if (parser.isValidNode(start)) {
            this.start = start;
        }
    }

    public void setStop(MapNode stop) {

        if (parser.isValidNode(stop)) {
            this.stop = stop;
        }

    }

    public Path AStar() {

        //+++++++++++++
        //this part of the function is where the searching takes place
        //+++++++++++++

        //set up the frontier, the PriorityQueue of all points that need to be searched
        SortedFrontier<MapNode> frontier = new SortedFrontier<>();
        frontier.put(this.start, 0);

        //set up the "log" as I'll call it, this tracks which Node you came from to get to the current node
        //this is how the pathfinder determines which path to follow, once it sees this.stop, it backtracks through
        //the HashMap to get the Node path
        HashMap<MapNode, MapNode> cameFrom = new HashMap<MapNode, MapNode>();
        cameFrom.put(this.start, start);

        //this HashMap tracks how "expensive" it is to get to a certain Node from this.start
        //it'll come into play during the searching
        HashMap<MapNode, Integer> costSoFar = new HashMap<MapNode, Integer>();
        costSoFar.put(this.start, 0);

        while (frontier.size() != 0) {

            MapNode current = frontier.pop(); //get the first Node in the frontier

            if (current.equals(this.stop)) { //if this is the destination node, stop searching
                break;
            }

            LinkedList<MapNode> neighbors = this.parser
                .getsTo(current); //get all the Nodes you can reach from this node

            for (MapNode n : neighbors) {

                int newCost = costSoFar.get(current) + n
                    .distanceTo(current); // it takes this many units to get here

                //if n hasn't been explored or it is cheaper to get to n this way
                //note the order of the if statement
                //if n is not in cost so far, the second term will error, but because the first term returns true in the same case,
                //the OR relation will prevent the error case from running
                if (!costSoFar.containsKey(n) || newCost < costSoFar.get(n)) {
                    costSoFar.put(n, newCost); // it took newCost to get here
                    frontier.put(n, newCost + n
                        .distanceTo(this.stop)); //Heuristic is the distance to the stop MapNode
                    cameFrom.put(n, current); //we went through Node current to get here
                }
            }
        }

        //++++++++++++
        //now the searching is done, the HashMap cameFrom will contain this.stop, so all that
        //is left to do is trace back through the HashMap to get the path
        //++++++++++++

        MapNode backTrace = this.stop; // we start tracing the path from the end
        LinkedList<MapNode> pathNodes = new LinkedList<MapNode>();
        LinkedList<MapEdge> pathEdges = new LinkedList<MapEdge>();

        while (!backTrace.equals(this.start)) {
            //System.out.println(cameFrom.get(backTrace).getID());
            pathNodes.addFirst(backTrace);
            pathEdges.addFirst(parser.getEdge(cameFrom.get(backTrace), backTrace));
            backTrace = cameFrom.get(backTrace);
        }
        pathEdges.addFirst(parser.getEdge(start, pathNodes.getFirst()));
        pathNodes.addFirst(this.start);

        Path path = new Path(pathNodes, pathEdges);
        return path;

    }
}
