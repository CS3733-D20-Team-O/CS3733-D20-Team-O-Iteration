package edu.wpi.onyx_ouroboros.model.service_requests;

import edu.wpi.onyx_ouroboros.view_model.service_requests.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reflections.Reflections;

@Slf4j
public class ServicesList {

  public static class Service {

    @Getter
    @Setter
    private String descriptionLangModelID;
    @Getter
    private final String fxmlFile;

    public Service(String descriptionLangModelID, String fxmlFile) {
      this.descriptionLangModelID = descriptionLangModelID;
      this.fxmlFile = fxmlFile;
    }
  }

  public static List<Service> getServices() {
    val methodName = "getDescriptionLangModelID";
    val packageName = "edu.wpi.onyx_ouroboros.view_model.service_requests.services";
    val serviceClasses = new Reflections(packageName).getSubTypesOf(ServiceRequest.class);
    val services = new ArrayList<Service>();
    for (val serviceClass : serviceClasses) {
      try {
        val langField = serviceClass.getMethod(methodName).invoke(null);
        val fxmlFile = "views/service_requests/" + serviceClass.getSimpleName();
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
