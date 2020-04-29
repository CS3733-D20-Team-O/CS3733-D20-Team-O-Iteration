package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NodeSelectorViewModel extends ViewModelBase {

  private final DatabaseWrapper database;

  @FXML
  private JFXComboBox<String> selector;

  private final Map<String, Node> descToNodes = new HashMap<>();

  @Setter
  private Consumer<Node> onNodeSelectedListener;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the internal list of nodes
    database.exportNodes().values().forEach(node ->
        descToNodes.put(String.format("(%s) %s", node.getFloor(), node.getLongName()), node));
    // Set the selector's options
    resetSelector();
    // Set the prompt text
    selector.getEditor().setPromptText("Select or search for a location");
    // Add a listener for when an item is selected
    selector.getSelectionModel().selectedItemProperty().addListener((o, old, newDesc) -> {
      if (newDesc != null && onNodeSelectedListener != null) {
        onNodeSelectedListener.accept(descToNodes.get(newDesc));
      }
    });
    // Add a listener for when the search text changes
    selector.getEditor().textProperty().addListener((o, old, newText) -> Platform.runLater(() -> {
      selector.getItems().clear();
      if (newText.isEmpty()) {
        resetSelector();
      } else {
        FuzzySearch.extractTop(newText, descToNodes.keySet(), 10).stream()
            .map(ExtractedResult::getString)
            .forEachOrdered(selector.getItems()::add);
      }
      selector.show();
      selector.getEditor().requestFocus();
    }));
  }

  /**
   * Resets the selectors options to what is in the internal map
   */
  private void resetSelector() {
    descToNodes.keySet().stream().sorted().forEachOrdered(selector.getItems()::add);
  }
}
