package edu.wpi.onyx_ouroboros.view_model.service_requests;

import edu.wpi.onyx_ouroboros.model.language.Language;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The base class for any service request fxml controllers
 */
public abstract class ServiceRequest extends ViewModelBase {

  ServiceRequest() {
    super();
    cancelButton.setOnAction(event -> ((Stage) cancelButton.getScene().getWindow()).close());
    submitButton.setOnAction(
        event -> {
          if (onSubmitPressed()) {
            ((Stage) submitButton.getScene().getWindow()).close();
          }
        });
    // todo style buttons
  }

  @Language(ID = "serviceSelectionSubmitButton")
  public Button submitButton = new Button();

  @Language(ID = "serviceSelectionCancelButton")
  public Button cancelButton = new Button();

  /**
   * Adds the control buttons
   *
   * @param pane the pane to add the buttons to
   */
  public void addControlButtons(Pane pane) {
    pane.getChildren().add(cancelButton);
    pane.getChildren().add(submitButton);
  }

  /**
   * Called when submit button is pressed
   *
   * @return whether or not submission was successful
   */
  public abstract boolean onSubmitPressed();
}
