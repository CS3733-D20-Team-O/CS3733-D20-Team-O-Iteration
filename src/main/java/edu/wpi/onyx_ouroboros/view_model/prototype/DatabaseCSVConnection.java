package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.val;

public class DatabaseCSVConnection extends ViewModelBase {

  @FXML TextField txtfieldFileLocation;
  @FXML Button btnImport;
  @FXML Button btnExport;

  private final CSVHandler csvHandler = DependencyInjector.create(CSVHandler.class);

  @FXML
  private void onExportClicked() {
    String fileLocation = txtfieldFileLocation.getText(); // Get the string from the text field
    csvHandler.exportFromDatabase(fileLocation);
  }

  @FXML
  private void onImportClicked() {
    // Delete everything in the database so we can import fresh
    val database = DependencyInjector.create(DatabaseWrapper.class);
    database.export().forEach((node) -> database.deleteNode(node.getNodeID()));

    String fileLocation = txtfieldFileLocation.getText(); // Get the string from the text field
    csvHandler.importToDatabase(fileLocation);
  }

  @FXML
  private void buttonToggle() {
    if(!txtfieldFileLocation.getText().isEmpty()) { // If the text field isn't empty
      btnImport.setDisable(false);
      btnExport.setDisable(false);
    } else { // If the text field is empty
      btnImport.setDisable(true);
      btnExport.setDisable(true);
    }
  }
}
