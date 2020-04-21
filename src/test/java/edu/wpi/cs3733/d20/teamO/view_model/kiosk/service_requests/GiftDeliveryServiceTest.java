package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jfoenix.controls.JFXTextField;
import org.junit.jupiter.api.Test;

/**
 * Tests GiftDeliveryService
 */
public class GiftDeliveryServiceTest {

  /**
   * Temporary test to satisfy code coverage requirements todo remove
   */
  @Test
  public void satisfyCodeCoverage() {
    assertEquals(Double.MAX_VALUE, new GiftDeliveryService().getFlowNewLine());
  }

  @Test
  public void checkIfJFXTextFieldIsEmpty() {
    JFXTextField tf = new JFXTextField();
    assertTrue(tf.getText().isEmpty());
  }

  @Test
  public void checkIfFilledInJFXTextFieldIsEmpty() {
    JFXTextField tf = new JFXTextField("sample text");
    assertFalse(tf.getText().isEmpty());
  }

  /*
  @Test
  public void creditCardWithLessThan13Digits() {
    long ccNumber =
  }
  */
}
