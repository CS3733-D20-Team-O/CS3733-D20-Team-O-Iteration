package edu.wpi.cs3733.d20.teamO.model.material.node_selector;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.val;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class NodeSelector extends JFXTextField {

  private final Map<String, Node> descToNodes = new HashMap<>();

  @Setter
  private Consumer<Node> onNodeSelectedListener;

  private final JFXAutoCompletePopup<String> popup = new JFXAutoCompletePopup<>();

  public NodeSelector() {
    // Set the pop up selection to set the text (and thus trigger calling the listener)
    popup.setSelectionHandler(e -> setText(e.getObject()));

    // Open up the pop up when the text field is given focus
    focusedProperty().addListener((o, old, newBool) -> {
      if (newBool) {
        setPopup();
      }
    });

    // Add a text listener that calls the node selected listener
    textProperty().addListener((o, old, newText) -> {
      if (descToNodes.containsKey(newText) && onNodeSelectedListener != null) {
        onNodeSelectedListener.accept(descToNodes.get(newText));
      }
      setPopup();
    });
  }

  /**
   * Sets the internal node listing based off of the supplied nodeMap
   */
  public void setNodes(Collection<Node> nodes) {
    descToNodes.clear();
    for (val node : nodes) {
      descToNodes.put(String.format("(%s) %s", node.getFloor(), node.getLongName()), node);
    }
  }

  /**
   * @return the currently selected Node, or null
   */
  public Node getSelectedNode() {
    return descToNodes.getOrDefault(getText(), null);
  }

  /**
   * Clears this node selector
   */
  @Override
  public void clear() {
    super.clear();
    popup.hide();
  }

  /**
   * Sets the pop up based on the current text and displays it
   */
  private void setPopup() {
    // Clear so we can repopulate with what we want
    popup.getSuggestions().clear();
    if (getText().isBlank()) {
      // Set with a sorted list of default options
      popup.getSuggestions().addAll(descToNodes.keySet().stream()
          .sorted().collect(Collectors.toList()));
    } else {
      // Set with a list of the sorted search options
      FuzzySearch.extractSorted(getText(), descToNodes.keySet()).stream()
          .map(ExtractedResult::getString)
          .forEachOrdered(popup.getSuggestions()::add);
    }
    // Show the pop up if it is not already
    popup.show(this);
  }
}
