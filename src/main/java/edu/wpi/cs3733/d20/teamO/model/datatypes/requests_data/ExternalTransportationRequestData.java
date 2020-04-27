package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

/**
 * Represents the extra data of an external transportation request
 */
@Value
public class ExternalTransportationRequestData implements ServiceRequestData {

  String transportationType, destination, notes;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return "Mode of Transportation: " + transportationType + "\nDestination: " + destination
        + "\nAdditional Notes: " + notes;
  }
}
