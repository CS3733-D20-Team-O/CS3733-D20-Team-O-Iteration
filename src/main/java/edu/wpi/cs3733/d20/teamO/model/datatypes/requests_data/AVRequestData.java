package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

/**
 * Represents the extra data of a sanitation request
 */
@Value
public class AVRequestData implements ServiceRequestData {

  String request, notes, timeLength, deliveryTime;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return "Service Requested: " + request + "\nDelivery Time: " + deliveryTime
        + "\nLength of time: " + timeLength + "\nAdditional Notes: " + notes;
  }
}