package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import lombok.val;

public class DatabaseCSVConnection extends ViewModelBase {

  private final CSVHandler csvHandler = DependencyInjector.create(CSVHandler.class);

  public void onExportClicked() {
    // todo implement this method and connect to FXML's Button onAction
    // csvHandler.exportFromDatabase(filename);
  }

  public void onImportClicked() {
    // Delete everything in the database so we can import fresh
    val database = DependencyInjector.create(DatabaseWrapper.class);
    database.export().forEach((node) -> database.deleteNode(node.getNodeID()));

    // todo implement the rest of this method and connect to FXML's Button onAction
    // csvHandler.importToDatabase(filename);
  }
}
