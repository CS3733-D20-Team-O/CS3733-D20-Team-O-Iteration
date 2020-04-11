package edu.wpi.onyx_ouroboros.model.astar.old;

import java.util.LinkedList;

public class SortedFrontier<T> {

    private final LinkedList<T> frontier;
    private final LinkedList<Integer> priority;

    /**
     * constructor
     */
    public SortedFrontier() {
        this.frontier = new LinkedList<T>();
        this.priority = new LinkedList<Integer>();
    }

    /**
     * adds a new Node to the frontier
     *
     * @param n      the Node to add
     * @param weight the weight of the Node
     */
    public void put(T n, int weight) {

        //zero size case
        if (frontier.size() == 0) {
            frontier.add(n);
            priority.add(weight);
            return;
        }

        int index = 0;
        for (int i = 0; i < priority.size(); i++) {
            if (priority.get(i)
                < weight) { //if the priority is less than the current, this is where it should go
                index = i;
                break;
            }
        }

        frontier.add(index, n);
        priority.add(index, weight);

    }

    /**
     * view the lowest priority Node in the frontier
     *
     * @return lowest priority node in the frontier
     */
    public T peek() {
        return frontier.getFirst();
    }


    /**
     * returns the size of the frontier
     *
     * @return number of Nodes in the frontier
     */
    public int size() {
        return frontier.size();
    }

    /**
     * removes the lowest priority Node from the frontier and returns it
     *
     * @return lowest priority Node in the frontier
     */
    public T pop() {
        T first = frontier.getLast();
        frontier.removeLast();
        priority.removeLast();
        return first;
    }
}
