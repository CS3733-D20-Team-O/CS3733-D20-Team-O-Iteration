package edu.wpi.onyx_ouroboros.view_model.database;

import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeDeletedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeInsertedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeUpdatedEvent;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import org.greenrobot.eventbus.Subscribe;

public class DisplayDatabaseViewModel extends ViewModelBase {

  @Subscribe
  public void onNodeDeleted(DatabaseNodeDeletedEvent event) {
    // todo finish the onNodeDeleted event
  }

  @Subscribe
  public void onNodeInsert(DatabaseNodeInsertedEvent event) {
    // todo finish the onNodeInsert event
  }

  @Subscribe
  public void onNodeUpdated(DatabaseNodeUpdatedEvent event) {
    // todo finish the onNodeDeleted event
  }
}
