package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

/**
 * Represents what every service request data object must supply
 */
public interface ServiceRequestData {

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  String getDisplayable();
}
