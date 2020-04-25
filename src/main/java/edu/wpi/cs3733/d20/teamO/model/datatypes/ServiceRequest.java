package edu.wpi.cs3733.d20.teamO.model.datatypes;

import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import lombok.Value;

/**
 * Represents a service request in the database
 */
@Value
public class ServiceRequest {

  String requestID, requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;

  /**
   * The associated extra data with this request
   */
  ServiceRequestData requestData;
}
