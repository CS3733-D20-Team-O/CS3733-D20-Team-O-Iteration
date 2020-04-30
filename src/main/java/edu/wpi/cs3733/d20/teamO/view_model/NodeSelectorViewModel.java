package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NodeSelectorViewModel extends ViewModelBase {

  private final DatabaseWrapper database;

  private final Map<String, Node> descToNodes = new HashMap<>();

  @Setter
  private Consumer<Node> onNodeSelectedListener;

  @FXML
  private JFXTextField selector;
  private final JFXAutoCompletePopup<String> popup = new JFXAutoCompletePopup<>();

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the internal map of nodes
    database.exportNodes().values().forEach(node ->
        descToNodes.put(String.format("(%s) %s", node.getFloor(), node.getLongName()), node));

    // Set the pop up selection to set the text (and thus trigger calling the listener)
    popup.setSelectionHandler(e -> selector.setText(e.getObject()));

    // Open up the pop up when the text field is given focus
    selector.focusedProperty().addListener((o, old, newBool) -> {
      if (newBool) {
        setPopup();
      }
    });

    // Add a text listener that calls the node selected listener
    selector.textProperty().addListener((o, old, newText) -> {
      if (descToNodes.containsKey(newText) && onNodeSelectedListener != null) {
        onNodeSelectedListener.accept(descToNodes.get(newText));
      }
      setPopup();
    });
  }

  /**
   * Sets the pop up based on the current text and displays it
   */
  private void setPopup() {
    // Clear so we can repopulate with what we want
    popup.getSuggestions().clear();
    if (selector.getText().isBlank()) {
      // Set with a sorted list of default options
      popup.getSuggestions().addAll(descToNodes.keySet().stream()
          .sorted().collect(Collectors.toList()));
    } else {
      // Set with a list of the sorted search options
      FuzzySearch.extractSorted(selector.getText(), descToNodes.keySet()).stream()
          .map(ExtractedResult::getString)
          .forEachOrdered(popup.getSuggestions()::add);
    }
    // Show the pop up if it is not already
    popup.show(selector);
  }
}
