package edu.wpi.onyx_ouroboros.view_model.service_requests.services;

import edu.wpi.onyx_ouroboros.view_model.service_requests.ServiceRequest;

public class SampleServiceRequest extends ServiceRequest {

  /**
   * @return the id of this service's description in LanguageModel
   */
  @SuppressWarnings("unused")
  public static String getDescriptionLangModelID() {
    return "mainWelcome";
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
