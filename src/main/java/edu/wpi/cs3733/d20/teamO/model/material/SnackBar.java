package edu.wpi.cs3733.d20.teamO.model.material;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import edu.wpi.cs3733.d20.teamO.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * A class that implements a material-styled snack bar
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SnackBar {

  private final Navigator navigator;

  /**
   * Shows a snackbar above the root view of the application
   *
   * @param text  the text to show in the snackbar
   * @param nodes extra nodes (such as buttons) to add to the end of the snack bar
   */
  public void show(String text, Node... nodes) {
    JFXSnackbar bar = new JFXSnackbar(navigator.getRoot());
    val label = new Label(text);
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    container.getChildren().addAll(nodes);
    container.setAlignment(Pos.CENTER);
    container.setSpacing(16);
    // Add 16 margin and 16 padding as per material design guidelines with total padding (w/ margin)
    container.setStyle("-fx-background-color: #323232; -fx-background-insets: 16; -fx-padding: 32");
    bar.enqueue(new SnackbarEvent(container));
  }
}
