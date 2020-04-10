package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.csv.CSVHandler;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;

public class ModifyDatabaseViewModel extends ViewModelBase {
  public void onAddClicked() {
    DependencyInjector.create(DatabaseWrapper.class).addNode(null);
    DependencyInjector.create(DatabaseWrapper.class).deleteNode(null);
    DependencyInjector.create(DatabaseWrapper.class).updateNode(null,null);
    DependencyInjector.create(CSVHandler.class).exportFromDatabase("FILELOCATION");
  }
}
