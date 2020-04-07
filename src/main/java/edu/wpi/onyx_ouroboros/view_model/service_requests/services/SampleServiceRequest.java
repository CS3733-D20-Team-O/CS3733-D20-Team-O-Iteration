package edu.wpi.onyx_ouroboros.view_model.service_requests.services;

import edu.wpi.onyx_ouroboros.view_model.service_requests.ServiceRequest;

public class SampleServiceRequest extends ServiceRequest {

  /**
   * @return the key for the description of this service
   */
  @SuppressWarnings("unused")
  public static String getDescriptionKey() {
    return "mainWelcome"; // NOTE: this is NOT a service request description key, just a valid key
  }

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
