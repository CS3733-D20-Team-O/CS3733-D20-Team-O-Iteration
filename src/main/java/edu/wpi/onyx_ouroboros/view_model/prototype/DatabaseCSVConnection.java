package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.val;

public class DatabaseCSVConnection extends ViewModelBase {

  @FXML TextField txtfieldFileLocation;
  private final CSVHandler csvHandler = DependencyInjector.create(CSVHandler.class);

  public void onExportClicked() {
    String fileLocation = txtfieldFileLocation.getText(); // Get the string from the text field
    csvHandler.exportFromDatabase(fileLocation);
  }

  public void onImportClicked() {
    // Delete everything in the database so we can import fresh
    val database = DependencyInjector.create(DatabaseWrapper.class);
    database.export().forEach((node) -> database.deleteNode(node.getNodeID()));

    String fileLocation = txtfieldFileLocation.getText(); // Get the string from the text field
    csvHandler.importToDatabase(fileLocation);
  }
}
