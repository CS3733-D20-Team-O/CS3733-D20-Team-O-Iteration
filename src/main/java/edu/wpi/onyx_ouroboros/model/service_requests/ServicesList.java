package edu.wpi.onyx_ouroboros.model.service_requests;

import edu.wpi.onyx_ouroboros.view_model.service_requests.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reflections.Reflections;

/**
 * A class that provides a list of usable service requests
 */
@Slf4j
public class ServicesList {

  /**
   * Represents a usable service
   */
  public static class Service {

    /**
     * The language model ID for the description of this service
     */
    @Getter
    private final String descriptionLangModelID;
    /**
     * The fxml file location of the service request user interface
     */
    @Getter
    private final String fxmlFile;

    /**
     * Constructs a representation of a service request
     *
     * @param descriptionLangModelID the language model ID for the description of this service
     * @param fxmlFile               the fxml file location of the service request user interface
     */
    private Service(String descriptionLangModelID, String fxmlFile) {
      this.descriptionLangModelID = descriptionLangModelID;
      this.fxmlFile = fxmlFile;
    }
  }

  /**
   * @return the list of usable services
   */
  public static List<Service> getServices() {
    val methodName = "getDescriptionLangModelID";
    val packageName = ServiceRequest.class.getPackageName() + ".services";
    val serviceClasses = new Reflections(packageName).getSubTypesOf(ServiceRequest.class);
    val services = new ArrayList<Service>();
    for (val serviceClass : serviceClasses) {
      try {
        val langField = serviceClass.getMethod(methodName).invoke(null);
        val fxmlFile = "views/service_requests/" + serviceClass.getSimpleName() + ".fxml";
        services.add(new Service((String) langField, fxmlFile));
      } catch (Exception e) {
        val errorMsg = "ServiceRequest " + serviceClass.getCanonicalName() +
            " does not have the required 'public static String " + methodName + "()'!";
        log.error(errorMsg, e);
      }
    }
    return services;
  }
}
