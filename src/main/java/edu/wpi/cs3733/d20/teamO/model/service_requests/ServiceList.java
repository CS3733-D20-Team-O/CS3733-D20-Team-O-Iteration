package edu.wpi.cs3733.d20.teamO.model.service_requests;

import lombok.Value;

/**
 * A class that provides a list of usable service requests
 */
public class ServiceList {

  /**
   * Represents a usable service
   */
  @Value
  public static class Service {

    /**
     * The key for the description of this service
     */
    String descriptionKey;
    /**
     * The fxml file location of the service request user interface
     */
    String fxmlFile;
  }

  /**
   * A list of usable service requests
   * <p>
   * ALL service requests must be registered here!
   */
  public static final Service[] SERVICES = {
      new Service("serviceGiftDeliveryDescription",
          "views/service_requests/GiftDeliveryService.fxml"),
  };
}
