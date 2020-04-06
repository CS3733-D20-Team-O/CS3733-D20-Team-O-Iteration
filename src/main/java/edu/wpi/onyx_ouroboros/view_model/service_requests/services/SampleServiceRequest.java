package edu.wpi.onyx_ouroboros.view_model.service_requests.services;

import edu.wpi.onyx_ouroboros.view_model.service_requests.ServiceRequest;

public class SampleServiceRequest extends ServiceRequest {

  /**
   * Called when submit button is pressed
   *
   * @return whether or not submission was successful
   */
  @Override
  public boolean onSubmitPressed() {
    return true;
  }
}
