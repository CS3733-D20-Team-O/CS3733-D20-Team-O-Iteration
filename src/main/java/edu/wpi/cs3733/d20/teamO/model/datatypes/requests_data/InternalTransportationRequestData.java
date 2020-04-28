package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

/**
 * Represents the extra data of an internal transportation request
 */
@Value
public class InternalTransportationRequestData implements ServiceRequestData {

  String assistance, transportationType, destination;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return assistance + " transport is required" + "\nMode of transport: " + transportationType
        + "\nDestination: " + destination;
  }
}
