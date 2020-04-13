package edu.wpi.onyx_ouroboros;

import edu.wpi.onyx_ouroboros.events.Event;
import edu.wpi.onyx_ouroboros.events.RegisterViewModelEvent;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Slf4j
public abstract class ApplicationBase extends Application {

  /**
   * A list of the registered ViewModelBases to forward events to
   */
  private final List<WeakReference<ViewModelBase>> registeredViewModels = new LinkedList<>();

  @Override
  public void init() {
    log.info("Starting up " + getClass().getSimpleName());
    EventBus.getDefault().register(this);
  }

  @Override
  public void stop() {
    log.info("Stopping " + getClass().getSimpleName());
    EventBus.getDefault().unregister(this);
  }

  /**
   * Registers a ViewModel to receive events
   *
   * @param event the RegisterViewModelEvent that contains the ViewModel to register
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void registerViewModel(RegisterViewModelEvent event) {
    registeredViewModels.add(new WeakReference<>(event.getViewModel()));
  }

  /**
   * When an event is fired, forward it on to all ViewModelBases registered
   * <p>
   * This is a workaround for the lack of registerWeak in EventBus
   *
   * @param event the event fired
   */
  @Subscribe
  @SuppressWarnings("unused") // called by EventBus
  public void onEvent(Event event) {
    for (var i = registeredViewModels.listIterator(); i.hasNext(); ) {
      val viewModel = i.next().get();
      // if the object was garbage collected, remove it from the list; otherwise, forward event
      if (viewModel == null) {
        i.remove();
      } else {
        viewModel.onEventReceived(event);
      }
    }
  }
}
