package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.model.language.Language;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServiceRequestSelection extends ViewModelBase {

  @Language(key = "serviceSelectionSubmitButton")
  private final Button submitButton = new Button();

  @Language(key = "serviceSelectionCancelButton")
  private final Button cancelButton = new Button();

  public ServiceRequestSelection() {
    cancelButton.setOnAction(event -> ((Stage) cancelButton.getScene().getWindow()).close());
    submitButton.setOnAction(
        event -> {
          if (true) { // fixme call the child ServiceRequest viewModel's onSubmitPressed()
            ((Stage) submitButton.getScene().getWindow()).close();
          }
        });
    styleButtons();
  }

  /**
   * Styles the submit and cancel buttons
   */
  private void styleButtons() {
    // todo style buttons
  }
}
