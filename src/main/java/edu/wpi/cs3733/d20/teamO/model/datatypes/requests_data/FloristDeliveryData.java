package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

@Value
public class FloristDeliveryData implements ServiceRequestData {

  String bouquetType, price;

  /**
   * @return a multiline string fit for displaying directly to an end user
   */
  public String getDisplayable() {
    return "Bouquet:" + bouquetType + "For $" + price;
  }

}
