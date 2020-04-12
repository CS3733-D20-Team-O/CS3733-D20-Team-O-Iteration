package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;

public class DownloadDatabaseViewModel extends ViewModelBase {

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  public void test(){
    //empty
  }

}
