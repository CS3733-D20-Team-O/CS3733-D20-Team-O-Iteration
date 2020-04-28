package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

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
  Validator validator;
  @Mock
  SnackBar snackBar;
  @Mock
  Dialog dialog;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  GiftDeliveryService viewModel;

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


  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    populateFloorAndLocation();
//    populateGifts();
//    populateCreditCardBoxes();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/GiftDeliveryService.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  private void populateFloorAndLocation() {
    val map = new HashMap<String, Node>();
    map.put("a", new Node("a", 0, 0, 1, "", "", "Floor 1", ""));
    map.put("b", new Node("b", 0, 0, 3, "", "", "Floor 3-1", ""));
    map.put("c", new Node("c", 0, 0, 3, "", "", "Floor 3-2", ""));
    map.put("d", new Node("d", 0, 0, 5, "", "", "Floor 5", ""));
    when(database.exportNodes()).thenReturn(map);
  }

//  public void populateGifts() {
//    Map<String, String> giftList_Map = new HashMap<>();
//    giftList_Map.put("Stuffed Animal", "9.99");
//    giftList_Map.put("Card", "4.99");
//    giftList_Map.put("Box of Chocolates", "12.99");
//    giftList_Map.put("Toy", "3.99");
//  }

  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Select Floor");
    verifyThat("1", javafx.scene.Node::isVisible);
    verifyThat("2", javafx.scene.Node::isVisible);
    verifyThat("3", javafx.scene.Node::isVisible);
    verifyThat("4", javafx.scene.Node::isVisible);
    verifyThat("5", javafx.scene.Node::isVisible);

    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("1");
    verifyThat("1", javafx.scene.Node::isVisible);

    // Third floor
    clickOn("1");
    clickOn("3");
    clickOn("Floor 3-1");
    verifyThat("Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("Floor 3-2", javafx.scene.Node::isVisible);

    // Fifth floor
    clickOn("3");
    clickOn("5");
    verifyThat("5", javafx.scene.Node::isVisible);
  }

  @Test
  public void testItems1() {
    clickOn("Items");
    verifyThat("Stuffed Animal: $9.99", javafx.scene.Node::isVisible);
    verifyThat("Card: $4.99", javafx.scene.Node::isVisible);
    verifyThat("Box of Chocolates: $12.99", javafx.scene.Node::isVisible);
    verifyThat("Toy: $3.99", javafx.scene.Node::isVisible);

    clickOn("Stuffed Animal: $9.99");
    verifyThat("Stuffed Animal: $9.99", javafx.scene.Node::isVisible);
  }

  @Test
  public void testItems2() {
    clickOn("Items");
    verifyThat("Stuffed Animal: $9.99", javafx.scene.Node::isVisible);
    verifyThat("Card: $4.99", javafx.scene.Node::isVisible);
    verifyThat("Box of Chocolates: $12.99", javafx.scene.Node::isVisible);
    verifyThat("Toy: $3.99", javafx.scene.Node::isVisible);

    clickOn("Box of Chocolates: $12.99");
    verifyThat("Box of Chocolates: $12.99", javafx.scene.Node::isVisible);
  }

  @Test
  public void toNameTest1() {
    clickOn("Recipient Name");
    clickOn("Submit");
  }

  @Test
  public void testCCType1() {
    clickOn("Type");
    verifyThat("Visa", javafx.scene.Node::isVisible);
    verifyThat("Mastercard", javafx.scene.Node::isVisible);
    verifyThat("AMEX", javafx.scene.Node::isVisible);
    verifyThat("Discover", javafx.scene.Node::isVisible);
  }

  @Test
  public void testMMTest1() {
    clickOn("MM");
    verifyThat("01", javafx.scene.Node::isVisible);
    verifyThat("02", javafx.scene.Node::isVisible);
    verifyThat("03", javafx.scene.Node::isVisible);
    verifyThat("04", javafx.scene.Node::isVisible);
    verifyThat("05", javafx.scene.Node::isVisible);

  }

  @Test
  public void testYYYYTest1() {
    clickOn("YYYY");
    verifyThat("2020", javafx.scene.Node::isVisible);
    verifyThat("2021", javafx.scene.Node::isVisible);
    verifyThat("2022", javafx.scene.Node::isVisible);
    verifyThat("2023", javafx.scene.Node::isVisible);
    verifyThat("2024", javafx.scene.Node::isVisible);
  }


  @Test
  public void toFieldEmpty() {
    clickOn("Items");
    clickOn("Stuffed Animal: $9.99");
    clickOn("Submit");
    verify(validator, times(1)).validate(any());

    clickOn("Recipient Name");
    write("Getter Name");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());

    clickOn("Sender Name");
    write("SenderTest");
    clickOn("Submit");
    verify(validator, times(3)).validate(any());

    clickOn("Select Floor");
    clickOn("3");
    clickOn("Submit");
    verify(validator, times(4)).validate(any());

    clickOn("Select Room");
    clickOn("Floor 3-1");
    clickOn("Submit");
    verify(validator, times(5)).validate(any());

    write("\\tab");
    write("12:09");
    clickOn("Submit");
    verify(validator, times(6)).validate(any());

    clickOn("First Name On Card");
    write("FirstTest");
    clickOn("Submit");
    verify(validator, times(7)).validate(any());

    clickOn("Last Name On Card");
    write("LastTest");
    clickOn("Submit");
    verify(validator, times(8)).validate(any());

    clickOn("Type");
    clickOn("Visa");
    clickOn("Submit");
    verify(validator, times(9)).validate(any());

    clickOn("Card Number");
    write("1234567891234564");
    clickOn("Submit");
    verify(validator, times(10)).validate(any());

    clickOn("MM");
    clickOn("06");
    clickOn("Submit");
    verify(validator, times(11)).validate(any());

    clickOn("YYYY");
    clickOn("2023");
    clickOn("Submit");
    verify(validator, times(12)).validate(any());

    clickOn("CCV");
    write("456");
    clickOn("Submit");
    verify(validator, times(13)).validate(any());

    clickOn("Email Address for recipet");
    write("asdf@asdfff.com");
    clickOn("Submit");
    verify(validator, times(14)).validate(any());

    verifyThat("Total: 9.99", javafx.scene.Node::isVisible);


  }

  @Test
  public void everythingEmpty() {
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
  }

  /**
   * Test List Items - 2 Recipient Name - 3 Sender Name - 3 Select Floor - 2 Select Room - 3
   * Delivery Time - 3 First Name - 3 Last Name - 3 Type - 3 CC Num - 3 MM - 3 YYYY - 3 CCV - 3
   * Email - 4 Submit - 8 1. Nothing 2. Everything 3. Some 4. Misisng half 5. Missing other half 6.
   * Missing Boxes 7. Missing Fields 8. Only time, email, cc, ccv
   */

  @Test
  public void submitTest8() {
    when(validator.validate((any()))).thenReturn(false);
  }

//  @Test
//  public void testSubmit() {
//    when(validator.validate(any())).thenReturn(false).thenReturn(true).thenReturn(true);
//    when(database.addServiceRequest(any(), any(), any(), any(), any()))
//        .thenReturn(null).thenReturn("ABCDEFGH");
//
//    // Test when there are fields not filled out
//    clickOn("Submit");
//    verify(validator, times(1)).validate(any());
//    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
//    verify(snackBar, times(0)).show(anyString());
//    verify(dialog, times(0)).showBasic(any(), any(), any());
//
//    // Test when there are fields filled out (but adding fails)
//    clickOn("Your Name");
//    write("John Smith");
//    clickOn("Submit");
//    verify(validator, times(2)).validate(any());
//    verify(database, times(1)).addServiceRequest(anyString(),
//        eq("Floor 1"), eq("Sanitation"), eq("John Smith"),
//        eq(new GiftDeliveryRequestData("Dry Spill", "")));
//    verify(snackBar, times(1)).show(anyString());
//    verify(dialog, times(0)).showBasic(any(), any(), any());
//
//    // Test when there are fields filled out (and adding succeeds)
//    clickOn("Submit");
//    verify(validator, times(3)).validate(any());
//    verify(database, times(2)).addServiceRequest(anyString(),
//        eq("Floor 1"), eq("Sanitation"), eq("John Smith"),
//        eq(new GiftDeliveryRequestData("Dry Spill", "")));
//    verify(snackBar, times(1)).show(anyString());
//    verify(dialog, times(1))
//        .showBasic(anyString(), eq("Your confirmation code is:\nABCDEFGH"), anyString());
//  }

}
