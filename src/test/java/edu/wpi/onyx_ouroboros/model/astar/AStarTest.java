package edu.wpi.onyx_ouroboros.model.astar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AStarTest {

  private final static Map<String, Node> map = new HashMap<>();
  private final static AStar testAStar = new AStar(new NodeMap(null));

  @BeforeAll
  public static void beforeAllTests() {
    Node a = new NodeTestImpl("a", 0, 0),
        b = new NodeTestImpl("b", 3, 0),
        c = new NodeTestImpl("c", 0, 2),
        d = new NodeTestImpl("d", 1, 1),
        e = new NodeTestImpl("e", 4, 2),
        f = new NodeTestImpl("f", 4, 3),
        g = new NodeTestImpl("g", 2, 5),
        h = new NodeTestImpl("h", 4, 1);

    a.getNeighbors().addAll(Arrays.asList(b, c));
    b.getNeighbors().addAll(Arrays.asList(a, d, h));
    c.getNeighbors().addAll(Arrays.asList(a, d));
    d.getNeighbors().addAll(Arrays.asList(c, b, g, e));
    e.getNeighbors().addAll(Arrays.asList(d, f, h));
    f.getNeighbors().addAll(Arrays.asList(e, g));
    g.getNeighbors().addAll(Arrays.asList(d, f));
    h.getNeighbors().addAll(Arrays.asList(b, e));

    map.put("a", a);
    map.put("b", b);
    map.put("c", c);
    map.put("d", d);
    map.put("e", e);
    map.put("f", f);
    map.put("g", g);
    map.put("h", h);
  }

  @Test
  public void testGoingFromAToB() {
    List<Node> desired = Arrays.asList(map.get("a"), map.get("b"));
    List<Node> answer = testAStar.findPathBetween(map.get("a"), map.get("b"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromAToG() {
    List<Node> desired = Arrays.asList(map.get("a"), map.get("c"), map.get("d"), map.get("g"));
    List<Node> answer = testAStar.findPathBetween(map.get("a"), map.get("g"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromCToF() {
    List<Node> desired = Arrays.asList(map.get("c"), map.get("d"), map.get("e"), map.get("f"));
    List<Node> answer = testAStar.findPathBetween(map.get("c"), map.get("f"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromHToD() {
    List<Node> desired = Arrays.asList(map.get("h"), map.get("b"), map.get("d"));
    List<Node> answer = testAStar.findPathBetween(map.get("h"), map.get("d"));
    assertEquals(desired, answer);
  }
}
