package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.view_model.admin.RequestHandlerViewModel;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Tests GiftDeliveryService
 */
public class GiftDeliveryServiceTest {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  CSVHandler csvHandler;
  @InjectMocks
  RequestHandlerViewModel viewModel;

  /**
   * Temporary test to satisfy code coverage requirements todo remove
   */
  /*
  @Test
  public void satisfyCodeCoverage() {
    assertEquals(Double.MAX_VALUE, new GiftDeliveryService().getFlowNewLine());
  }
  */

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

  @Test
  public void comboboxTest() {

  }
}
