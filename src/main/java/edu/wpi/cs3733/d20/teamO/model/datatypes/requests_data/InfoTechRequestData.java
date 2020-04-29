package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

@Value
public class InfoTechRequestData implements ServiceRequestData {

  String issueType, description;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return "IT Issue: " + issueType + "\n Description: " + description;
  }
}