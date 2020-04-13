package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.events.database.DatabaseNodeDeletedEvent;
import edu.wpi.onyx_ouroboros.events.database.DatabaseNodeInsertedEvent;
import edu.wpi.onyx_ouroboros.events.database.DatabaseNodeUpdatedEvent;
import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import lombok.val;
import org.greenrobot.eventbus.Subscribe;

public class DisplayDatabaseViewModel extends ViewModelBase {

  @Override
  protected void start(URL location) {
    deleteAll();
    val currentNodes = DependencyInjector.create(DatabaseWrapper.class).export();
    addAll(currentNodes);
  }

  private void deleteAll() {
    // todo delete every node from displayed table
  }

  private void addAll(List<PrototypeNode> nodes) {
    // todo add every node to displayed table
  }

  @Subscribe
  public void onNodeDeleted(DatabaseNodeDeletedEvent event) {
    // event.getNodeID() returns nodeID String
    // todo finish the onNodeDeleted event
  }

  @Subscribe
  public void onNodeInsert(DatabaseNodeInsertedEvent event) {
    // event.getNode() will return the new node's properties
    // todo finish the onNodeInsert event
  }

  @Subscribe
  public void onNodeUpdated(DatabaseNodeUpdatedEvent event) {
    // event.getNodeID() will return the ID of the old node (so you know which one to update
    // event.getNode() contains all the new properties
    // todo finish the onNodeDeleted event
  }
}
