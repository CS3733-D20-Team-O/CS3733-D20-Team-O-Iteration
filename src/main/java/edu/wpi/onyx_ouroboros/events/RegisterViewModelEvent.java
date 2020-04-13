package edu.wpi.onyx_ouroboros.events;

import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import lombok.Value;

/**
 * An event that specifies that a ViewModel wants to register to receive events from an Application
 */
@Value
public class RegisterViewModelEvent implements Event {

  /**
   * The ViewModel that wants to register
   */
  ViewModelBase viewModel;
}
