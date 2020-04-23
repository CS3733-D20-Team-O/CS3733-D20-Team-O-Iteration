package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;

/**
 * Tests GiftDeliveryService
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class GiftDeliveryServiceTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  CSVHandler csvHandler;
  @InjectMocks
  GiftDeliveryService viewModel;

  /*
  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    stage
        .setScene(new Scene(loader.load(Main.class.getResourceAsStream("views/kiosk/ServiceRequestSelection.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }*/

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
