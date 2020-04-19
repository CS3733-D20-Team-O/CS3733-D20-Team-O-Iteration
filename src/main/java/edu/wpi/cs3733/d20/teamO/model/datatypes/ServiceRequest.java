package edu.wpi.cs3733.d20.teamO.model.datatypes;

import lombok.Setter;
import lombok.Value;

/**
 * Represents a service request in the database
 */
@Value
public class ServiceRequest {

  @Setter
  String requestID, requestTime, requestNode, type, requesterName, whoMarked, employeeAssigned;
}
