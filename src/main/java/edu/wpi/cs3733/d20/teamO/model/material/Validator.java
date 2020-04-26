package edu.wpi.cs3733.d20.teamO.model.material;

import com.jfoenix.controls.base.IFXValidatableControl;
import java.util.stream.Stream;
import javafx.scene.Node;

/**
 * A simply utility class that validates a series of supplied nodes
 */
public class Validator {

  /**
   * Validates all passed in fields, giving focus to the first invalid field and returning true if
   * and only if all inputs are valid (as per their validators)
   *
   * @param nodes the nodes to test for validation
   * @return true if and only if all the supplied nodes are valid (as per their validators)
   */
  @SafeVarargs
  public final <T extends Node & IFXValidatableControl> boolean validate(T... nodes) {
    // firstInvalid (the accumulating result) is set to the first invalid node in the stream,
    //  so if it is null that means every node is valid (it was never set to an invalid node)
    return null == Stream.of(nodes).reduce(null, (firstInvalid, node) -> {
      if (!node.validate() && firstInvalid == null) { // keep node.validate() first so it is called
        (firstInvalid = node).requestFocus(); // request focus on the first invalid node
      }
      return firstInvalid;
    });
  }
}
