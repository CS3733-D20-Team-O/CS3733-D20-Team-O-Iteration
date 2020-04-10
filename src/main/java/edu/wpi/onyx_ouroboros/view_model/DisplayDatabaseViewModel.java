package edu.wpi.onyx_ouroboros.view_model;

import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeDeletedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeInsertedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeUpdatedEvent;
import org.greenrobot.eventbus.Subscribe;

public class DisplayDatabaseViewModel extends ViewModelBase {
  @Subscribe
  public void onNodeDeleted(DatabaseNodeDeletedEvent event) {

  }

  @Subscribe
  public void onNodeInsert(DatabaseNodeInsertedEvent event) {

  }

  @Subscribe
  public void onNodeDeleted(DatabaseNodeUpdatedEvent event) {

  }
}
