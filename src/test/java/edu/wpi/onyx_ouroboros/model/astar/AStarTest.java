package edu.wpi.onyx_ouroboros.model.astar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class AStarTest {

  private final static Map<String, Node> map = new HashMap<>();
  private final static AStar testAStar = new AStar(new NodeMap(null));

  @BeforeAll
  public static void before(){
    NodeTestImplementation a = new NodeTestImplementation("a", 0, 0);
    NodeTestImplementation b = new NodeTestImplementation("b", 3, 0);
    NodeTestImplementation c = new NodeTestImplementation("c", 0, 2);
    NodeTestImplementation d = new NodeTestImplementation("d", 1, 1);
    NodeTestImplementation e = new NodeTestImplementation("e", 4, 2);
    NodeTestImplementation f = new NodeTestImplementation("f", 4, 3);
    NodeTestImplementation g = new NodeTestImplementation("g", 2, 5);
    NodeTestImplementation h = new NodeTestImplementation("h", 4, 1);

    LinkedList<Node> aList = new LinkedList<>(); aList.add(b); aList.add(c);
    a.setNeighbors(aList);

    LinkedList<Node> bList = new LinkedList<>(); bList.add(a); bList.add(d); bList.add(h);
    b.setNeighbors(bList);

    LinkedList<Node> cList = new LinkedList<>(); cList.add(a); cList.add(d);
    c.setNeighbors(cList);

    LinkedList<Node> dList = new LinkedList<>(); dList.add(c); dList.add(b); dList.add(g); dList.add(e);
    d.setNeighbors(dList);

    LinkedList<Node> eList = new LinkedList<>(); eList.add(d); eList.add(f); eList.add(h);
    e.setNeighbors(eList);

    LinkedList<Node> fList = new LinkedList<>(); fList.add(e); fList.add(g);
    f.setNeighbors(fList);

    LinkedList<Node> gList = new LinkedList<>(); gList.add(d); gList.add(f);
    g.setNeighbors(gList);

    LinkedList<Node> hList = new LinkedList<>(); hList.add(b); hList.add(e);
    h.setNeighbors(hList);

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
  public void testSamePathWithSamePath(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("a"));
    desired.add(map.get("d"));
    desired.add(map.get("c"));

    List<Node> answer = new LinkedList<>();
    answer.add(map.get("a"));
    answer.add(map.get("d"));
    answer.add(map.get("c"));

    assertTrue(samePath(desired, answer));
  }

  @Test
  public void testSamePathWithEmptyPaths(){
    LinkedList<Node> desired = new LinkedList<>();
    List<Node> answer = new LinkedList<>();

    assertTrue(samePath(desired, answer));
  }

  @Test
  public void testSamePathWithDifferentOrderButSameNodes(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("a"));
    desired.add(map.get("b"));

    List<Node> answer = new LinkedList<>();
    answer.add(map.get("b"));
    answer.add(map.get("a"));

    assertFalse(samePath(desired, answer));
  }

  @Test
  public void testSamePathWithDifferentPaths(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("a"));
    desired.add(map.get("b"));

    List<Node> answer = new LinkedList<>();
    answer.add(map.get("c"));
    answer.add(map.get("d"));

    assertFalse(samePath(desired, answer));
  }

  @Test
  public void testGoingFromAToB(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("a"));
    desired.add(map.get("b"));

    List<Node> answer = testAStar.findPathBetween(map.get("a"), map.get("b"));

    assertTrue(samePath(desired, answer));
  }

  @Test
  public void testGoingFromAToG(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("a"));
    desired.add(map.get("c"));
    desired.add(map.get("d"));
    desired.add(map.get("g"));

    List<Node> answer = testAStar.findPathBetween(map.get("a"), map.get("g"));

    assertTrue(samePath(desired, answer));
  }

  @Test
  public void testGoingFromCToF(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("c"));
    desired.add(map.get("d"));
    desired.add(map.get("e"));
    desired.add(map.get("f"));

    List<Node> answer = testAStar.findPathBetween(map.get("c"), map.get("f"));

    assertTrue(samePath(desired, answer));
  }

  @Test
  public void testGoingFromHToD(){
    LinkedList<Node> desired = new LinkedList<>();
    desired.add(map.get("h"));
    desired.add(map.get("b"));
    desired.add(map.get("d"));

    List<Node> answer = testAStar.findPathBetween(map.get("h"), map.get("d"));

    assertTrue(samePath(desired, answer));
  }

  /**
   * compares two lists of Nodes to see if they are equal
   * @param solution desired list
   * @param answer unknown list
   * @return true if for solution and answer are the same size as well as if for any position i in solution there exists the same Node in position i in answer
   */
  public static boolean samePath(LinkedList<Node> solution, List<Node> answer){

    if(solution.size() != answer.size()){
      return false;
    }

    for(int i = 0; i < answer.size(); i++){
      System.out.println("Solution " + i + " equals " + solution.get(i).getID());
      System.out.println("Answer " + i + " equals " + answer.get(i).getID());

      if(!solution.get(i).equals(answer.get(i))){
        return false;
      }
    }
    return true;
  }
}
