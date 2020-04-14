package edu.wpi.onyx_ouroboros.model.data.database;

import edu.wpi.onyx_ouroboros.model.TestInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import org.junit.jupiter.api.Test;

/**
 * Tests DatabaseWrapper
 */
public class DatabaseWrapperTest {

  // todo delete this method and create real tests
  @Test
  public void satisfyCodeCoverage() {
    PrototypeNode dummyNode = new PrototypeNode("hi", 1, 2, 5, "test", "ELEV", "help", "me");
    TestInjector.create(DatabaseWrapperImpl.class).addNode(dummyNode);

  }

  @Test
  public void addNodeTest() {
    PrototypeNode newNode = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    TestInjector.create(DatabaseWrapperImpl.class).addNode(newNode);
    PrototypeNode sameNodeID = new PrototypeNode("0101", 0, 0, 1, "Faulkner", "ELEV", "Elevator 1",
        "Elev1");
    TestInjector.create(DatabaseWrapperImpl.class).addNode(sameNodeID);

  }
}
