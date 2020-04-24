package edu.wpi.cs3733.d20.teamO.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.path_finding.AStar;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AStarTest {

  private final static Map<String, Node> map = new HashMap<>();

  private static Node newNode(String id, int x, int y) {
    return new Node(id, x, y, 0, null, null, null, null);
  }

  @BeforeAll
  public static void beforeAllTests() {
    final Node a = newNode("a", 0, 0),
        b = newNode("b", 3, 0),
        c = newNode("c", 0, 2),
        d = newNode("d", 1, 1),
        e = newNode("e", 4, 2),
        f = newNode("f", 4, 3),
        g = newNode("g", 2, 5),
        h = newNode("h", 4, 1),
        // n is an unreachable node to test when no path can be found
        n = newNode("n", 1, 0);

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
    map.put("n", n);
  }

  @Test
  public void testGoingFromAToB() {
    final List<Node> desired = Arrays.asList(map.get("a"), map.get("b"));
    final List<Node> answer = AStar.findPathBetween(map.get("a"), map.get("b"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromAToG() {
    final List<Node> desired = Arrays
        .asList(map.get("a"), map.get("c"), map.get("d"), map.get("g"));
    final List<Node> answer = AStar.findPathBetween(map.get("a"), map.get("g"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromCToF() {
    final List<Node> desired = Arrays
        .asList(map.get("c"), map.get("d"), map.get("e"), map.get("f"));
    final List<Node> answer = AStar.findPathBetween(map.get("c"), map.get("f"));
    assertEquals(desired, answer);
  }

  @Test
  public void testGoingFromHToD() {
    final List<Node> desired = Arrays.asList(map.get("h"), map.get("b"), map.get("d"));
    final List<Node> answer = AStar.findPathBetween(map.get("h"), map.get("d"));
    assertEquals(desired, answer);
  }

  @Test
  public void testUnreachableAsStop() {
    final List<Node> answer = AStar.findPathBetween(map.get("h"), map.get("n"));
    assertNull(answer);
  }

  @Test
  public void testUnreachableAsStart() {
    final List<Node> answer = AStar.findPathBetween(map.get("n"), map.get("c"));
    assertNull(answer);
  }
}
