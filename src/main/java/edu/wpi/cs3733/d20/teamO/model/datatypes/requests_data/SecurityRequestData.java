package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;
import lombok.Value;

/**
 * Represents the extra data of an Emergency Security request
 */
@Value
public class SecurityRequestData implements ServiceRequestData {
  String emergencyType, notes;

  /**
   * Gets the information for the emergency type
   *
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return "Emergency Type: " + emergencyType + "\nAdditional Notes: " + notes;
  }
}