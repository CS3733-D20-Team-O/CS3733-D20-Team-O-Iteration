package edu.wpi.cs3733.d20.teamO.model.material;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * A class that implements a customized material-styled dialog
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Dialog {

  private final Navigator navigator;
  private final FXMLLoader fxmlLoader;

  /**
   * Shows a dialog above the root StackPane specified only
   *
   * @param root    the stack pane to show a dialog over
   * @param content the content to display in the dialog
   * @return the dialog being displayed on root
   */
  public JFXDialog show(StackPane root, Region content) {
    val dialog = new JFXDialog();
    dialog.setContent(content);
    dialog.show(root);
    return dialog;
  }

  /**
   * Shows a full screen dialog (covering the navigation bar, if present)
   *
   * @param content the content of the dialog
   * @return the dialog being displayed on the root
   */
  public JFXDialog showFullscreen(Region content) {
    return show(navigator.getRoot(), content);
  }

  /**
   * Creates a basic dialog with the given title, message, and a close button todo style more
   *
   * @param title           the title to display at the top of the dialog
   * @param message         the message to display in the dialog
   * @param closeButtonText the text to display in the close button (this is for localization)
   */
  public void showBasic(String title, String message, String closeButtonText) {
    val dialog = new JFXDialog();
    val closeButton = new JFXButton(closeButtonText);
    closeButton.setOnAction(e -> dialog.close());
    val buttonContainer = new HBox(closeButton);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
    val container = new VBox(new Label(title), new Label(message), buttonContainer);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(16));
    container.setSpacing(16);
    dialog.setContent(container);
    dialog.show(navigator.getRoot());
  }

  /**
   * Displays a fullscreen dialog feeding from the given fxml file
   *
   * @param fxml the fxml file to read
   * @throws IOException        in case the fxml file cannot be read
   * @throws ClassCastException in case the view model of the fxml is not a DialogViewModel
   */
  public void showFullscreenFXML(String fxml) throws IOException, ClassCastException {
    // Clear any previously usage of the fxmlLoader
    fxmlLoader.setRoot(null);
    fxmlLoader.setController(null);
    // Show the dialog in full screen mode and connect the view model to the dialog
    ((DialogViewModel) fxmlLoader.getController()).parent =
        showFullscreen(fxmlLoader.load(Main.class.getResourceAsStream(fxml)));
  }

  /**
   * A class that all fxml-loaded fxml-loaded dialog view models MUST extend
   */
  public static class DialogViewModel extends ViewModelBase {

    /**
     * The parent to this dialog
     */
    private JFXDialog parent;

    /**
     * Closes this dialog
     */
    protected final void close() {
      parent.close();
    }
  }
}
