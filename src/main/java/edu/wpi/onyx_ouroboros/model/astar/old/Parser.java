package edu.wpi.onyx_ouroboros.model.astar.old;

import java.util.LinkedList;

public class Parser {

    /**
     * class variables
     */
    private final LinkedList<MapNode> nodes;
    private final LinkedList<MapEdge> edges;

    /**
     * constructor
     */
    public Parser() {
        this.nodes = new LinkedList<MapNode>();
        this.edges = new LinkedList<MapEdge>();
    }

    /**
     * this function will populate the lists with random stuff just to be able to make a test map
     */
    public void create() {

        /*
                see Map.png
         */

        MapNode a = new MapNode("a", 0, 0);
        MapNode b = new MapNode("b", 3, 0);
        MapNode c = new MapNode("c", 2, 0);
        MapNode d = new MapNode("d", 1, 1);
        MapNode e = new MapNode("e", 4, 2);
        MapNode f = new MapNode("f", 4, 3);
        MapNode g = new MapNode("g", 2, 5);
        MapNode h = new MapNode("h", 4, 1);
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);
        nodes.add(e);
        nodes.add(f);
        nodes.add(g);
        nodes.add(h);

        MapEdge ab = new MapEdge("ab", a, b);
        MapEdge ac = new MapEdge("ac", a, c);
        edges.add(ab);
        edges.add(ac);

        MapEdge ba = new MapEdge("ba", b, a);
        MapEdge bd = new MapEdge("bd", b, d);
        MapEdge bh = new MapEdge("bh", b, h);
        edges.add(ba);
        edges.add(bd);
        edges.add(bh);

        MapEdge ca = new MapEdge("ca", c, a);
        MapEdge cd = new MapEdge("cd", c, d);
        edges.add(ca);
        edges.add(cd);

        MapEdge dc = new MapEdge("dc", d, c);
        MapEdge db = new MapEdge("db", d, b);
        MapEdge dg = new MapEdge("dg", d, g);
        MapEdge de = new MapEdge("de", d, e);
        edges.add(dc);
        edges.add(db);
        edges.add(de);
        edges.add(dg);

        MapEdge ed = new MapEdge("ed", e, d);
        MapEdge ef = new MapEdge("ef", e, f);
        MapEdge eh = new MapEdge("eh", e, h);
        edges.add(ed);
        edges.add(ef);
        edges.add(eh);

        MapEdge fe = new MapEdge("fe", f, g);
        MapEdge fg = new MapEdge("fg", f, g);
        edges.add(fe);
        edges.add(fg);

        MapEdge gf = new MapEdge("gf", g, f);
        MapEdge gd = new MapEdge("gd", g, d);
        edges.add(gf);
        edges.add(gd);

        MapEdge he = new MapEdge("he", h, e);
        MapEdge hb = new MapEdge("hb", h, b);
        edges.add(he);
        edges.add(hb);
    }

    /**
     * generates a list of all the reachable MapNodes from supplied MapNode
     *
     * @param node initial node
     * @return list of all nodes reachable from node
     */
    public LinkedList<MapNode> getsTo(MapNode node) {
        LinkedList<MapNode> reachable = new LinkedList<MapNode>();

        for (MapEdge e : edges) {
            if (e.isStart(node)) {
                reachable.add(e.getStop());
            }
        }

        return reachable;
    }

    /**
     * returns if the given node is valid meaning... idk yet, for now that it is in the list of nodes
     * and at least one edge goes to it
     *
     * @param node
     * @return
     */
    public boolean isValidNode(MapNode node) {
        boolean exists = false;
        for (MapNode n : nodes) {
            if (n.equals(node)) {
                exists = true;
                break;
            }
        }

        boolean reachable = false;
        for (MapEdge e : edges) {
            if (e.getStop().equals(node)) {
                reachable = true;
                break;
            }
        }

        return exists && reachable;
    }

    /**
     * get the edge going from start to stop
     *
     * @param start the initial MapNode in the MapEdge
     * @param stop  the destination MapNode in the MapEdge
     * @return the edge with matching start and stop, null otherwise
     */
    public MapEdge getEdge(MapNode start, MapNode stop) {
        for (MapEdge e : edges) {
            if (e.isStart(start) && e.isStop(stop)) {
                return e;
            }
        }

        return null;
    }

    /**
     * get MapNode with matching ID
     *
     * @param ID the ID to search for
     * @return MapNode with matching ID
     */
    public MapNode getNode(String ID) {
        MapNode node = null;

        for (MapNode n : nodes) {
            if (n.getID().equals(ID)) {
                node = n;
            }
        }

        if (node == null) {
            return null;
        }

        boolean reachable = false;
        for (MapEdge e : edges) {
            if (e.getStop().equals(node)) {
                reachable = true;
                break;
            }
        }
        if (!reachable) {
            return null;
        }

        return node;
    }
}
