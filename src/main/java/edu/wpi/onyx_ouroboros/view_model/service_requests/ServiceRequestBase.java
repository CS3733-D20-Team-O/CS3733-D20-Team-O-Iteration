package edu.wpi.onyx_ouroboros.view_model.service_requests;

import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;

/**
 * The base class for any service request fxml controllers
 */
public abstract class ServiceRequestBase extends ViewModelBase {

  /**
   * Called when submit button is pressed
   *
   * @return whether or not submission was successful
   */
  public abstract boolean onSubmitPressed();
}
