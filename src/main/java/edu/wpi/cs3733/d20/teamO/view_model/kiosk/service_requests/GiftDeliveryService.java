package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

public class GiftDeliveryService extends ServiceRequestBase {

  /**
   * @return the width to make Panes in FlowPane cause the FlowPane to wrap to the next line
   */
  public double getFlowNewLine() {
    return Double.MAX_VALUE;
  }

  /**
   * Called when submit button is pressed
   *
   * @return whether or not submission was successful
   */
  @Override
  public boolean onSubmitPressed() {
    // todo
    return false;
  }
}
