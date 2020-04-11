package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;

public class ModifyDatabaseViewModel extends ViewModelBase {

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  // Do whatever you need with connecting UI and this class (do not modify other classes) to make
  // these methods. This purposefully gives you a lot of flexibility to design the UI as you want.
  // Thus, feel free to change the method names and the parameters they take below, but make sure
  // this class provides the appropriate functionality (add, update, & delete)

  public void addNode() {
    // todo add node to database
    // database.addNode(prototypeNode); may be helpful
  }

  public void updateNode() {
    // todo update node in database
    // database.updateNode(nodeID, prototypeNode); may be helpful
  }

  public void deleteNode() {
    // todo delete node from database
    // database.deleteNode(nodeID); may be helpful
  }
}
