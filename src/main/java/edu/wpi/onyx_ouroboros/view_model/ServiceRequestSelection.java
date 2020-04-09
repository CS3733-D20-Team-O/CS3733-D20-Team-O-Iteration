package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.language.Language;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServiceRequestSelection extends ViewModelBase {

  @Language(key = "serviceSelectionSubmitButton")
  private final Button submitButton = new Button();

  @Language(key = "serviceSelectionCancelButton")
  private final Button cancelButton = new Button();

  public ServiceRequestSelection() {
    super();
    cancelButton.setOnAction(event -> ((Stage) cancelButton.getScene().getWindow()).close());
    submitButton.setOnAction(
        event -> {
          if (true) { // fixme call the child ServiceRequest viewModel's onSubmitPressed()
            ((Stage) submitButton.getScene().getWindow()).close();
          }
        });
    // todo style buttons
  }
}
