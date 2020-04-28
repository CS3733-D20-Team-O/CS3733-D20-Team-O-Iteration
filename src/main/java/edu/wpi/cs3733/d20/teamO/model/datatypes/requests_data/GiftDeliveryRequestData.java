package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;


import lombok.Value;

/**
 * Represents the extra data of a sanitation request
 */
@Value
public class GiftDeliveryRequestData implements ServiceRequestData {

  String giftName, recipientName;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  @Override
  public String getDisplayable() {
    return "Gift Name: " + giftName + "\nRecipient Name: " + recipientName;
  }
}

