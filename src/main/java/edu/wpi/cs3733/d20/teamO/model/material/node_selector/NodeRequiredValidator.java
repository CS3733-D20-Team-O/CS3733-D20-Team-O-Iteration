package edu.wpi.cs3733.d20.teamO.model.material.node_selector;

import com.jfoenix.validation.base.ValidatorBase;
import lombok.val;

/**
 * A validator for node selectors that ensures they have a valid node selected
 */
public class NodeRequiredValidator extends ValidatorBase {

  public NodeRequiredValidator() {
    super();
  }

  public NodeRequiredValidator(String message) {
    super(message);
  }

  @Override
  protected void eval() {
    val nodeSelector = (NodeSelector) srcControl.get();
    hasErrors.set(nodeSelector.getSelectedNode() == null);
  }
}
