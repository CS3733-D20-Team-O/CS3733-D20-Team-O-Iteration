package edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data;

import lombok.Value;

@Value
public class MedicineDeliveryServiceData implements ServiceRequestData {

  String medicineType, deliveryMethod;

  @Override
  public String getDisplayable() {
    return "Medicine type: " + medicineType + "\nDelivery method: " + deliveryMethod;
  }
}
