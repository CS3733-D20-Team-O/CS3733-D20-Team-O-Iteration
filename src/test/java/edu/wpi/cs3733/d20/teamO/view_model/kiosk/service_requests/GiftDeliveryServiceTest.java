package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.GiftDeliveryRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
  @Mock
  RequestConfirmationViewModel requestConfirmationViewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  GiftDeliveryService viewModel;

  private void initializeBundle() {
    //General use Bundles
    bundle.put("serviceGiftDeliveryDescription ", "Gift Delivery Service");
    bundle.put("floorPrompt", "Floor");
    bundle.put("floorPromptValidator", "A Floor is Required for the Service Request!");
    bundle.put("locationPrompt", "Room/Location on Floor");
    bundle.put("locationPromptValidator", "A Room or Location is Required for the Service Request!");
    bundle.put("submitButton", "Submit");
    bundle.put("cancelButton", "Cancel");

    //Unique Bundles
    bundle.put("availableGifts", "Gifts Available for Purchase");
    bundle.put("giftItems", "Items");
    bundle.put("serviceGiftDeliveryToField", "Recipient Name");
    bundle.put("serviceGiftDeliveryToFieldValidator", "Input is Required");
    bundle.put("serviceGiftDeliveryFromField", "Sender Name");
    bundle.put("giftDeliveryTime", "Delivery Time");
    bundle.put("giftDeliveryTimeValidator", "A time is Required for Delivery");
    bundle.put("giftPaymentDetails", "Payment Details");
    bundle.put("serviceGiftDeliveryFirstNameField", "First Name On Credit Card");
    bundle.put("serviceGiftDeliveryFirstNameFieldValidator", "Card Holder's name is Required");
    bundle.put("serviceGiftDeliveryLastNameField", "Last Name On Card");
    bundle.put("cardType", "Type of Card");
    bundle.put("serviceGiftDeliveryCCNumberField", "Credit Card Number");
    bundle.put("serviceGiftDeliveryEmailAddressField", "Email Address for receipt");
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


  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    bundle.put("nodeSelectorPromptText", "Select or search for a location");
    populateFloorAndLocation();
    initializeBundle();
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
    map.put("a", new Node("a", 0, 0, "1", "", "", "Floor 1", ""));
    map.put("b", new Node("b", 0, 0, "3", "", "", "Floor 3-1", ""));
    map.put("c", new Node("c", 0, 0, "3", "", "", "Floor 3-2", ""));
    map.put("d", new Node("d", 0, 0, "5", "", "", "Floor 5", ""));
    when(database.exportNodes()).thenReturn(map);
  }

  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Select or search for a location");
    verifyThat("(1) Floor 1", javafx.scene.Node::isVisible);
    verifyThat("(3) Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("(5) Floor 5", javafx.scene.Node::isVisible);

    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("(1) Floor 1");
    clickOn("(1) Floor 1");
    for (int i = 0; i < 8; i++) {
      press(KeyCode.BACK_SPACE);
      release(KeyCode.BACK_SPACE);
      System.out.println("in loop " + i);
    }
    write("1");
//    clickOn("Sender Name");
//    clickOn("Select or search for a location");
//    write("(1)");
    verifyThat("(1) Floor 1", javafx.scene.Node::isVisible);
    clickOn("(1) Floor 1");

    System.out.println("Testing Third Floor");
    // Third floor
    clickOn("(1) Floor 1");
    for (int i = 0; i < 8; i++) {
      press(KeyCode.BACK_SPACE);
      release(KeyCode.BACK_SPACE);
      System.out.println("in loop " + i);
    }
    write("3");
    verifyThat("(3) Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("(3) Floor 3-2", javafx.scene.Node::isVisible);
    clickOn("(3) Floor 3-1");

    System.out.println("Testing Fifth Floor");
    // Fifth floor
    clickOn("(3) Floor 3-1");
    for (int i = 0; i < 8; i++) {
      press(KeyCode.BACK_SPACE);
      release(KeyCode.BACK_SPACE);
      System.out.println("in loop " + i);
    }
    write("5");
    verifyThat("(5) Floor 5", javafx.scene.Node::isVisible);
    clickOn("(5) Floor 5");
    verifyThat("(5) Floor 5", javafx.scene.Node::isVisible);
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
    clickOn("Type of Card");
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
  public void fullTest() {
    when(validator.validate(any())).thenReturn(true);
    clickOn("Items");
    clickOn("Stuffed Animal: $9.99");
//    clickOn("Submit");
//    verify(validator, times(1)).validate(any());

    clickOn("Recipient Name");
    write("Getter Name");
//    clickOn("Submit");
//    verify(validator, times(2)).validate(any());

    clickOn("Sender Name");
    write("Sender Name");
//    clickOn("Submit");
//    verify(validator, times(3)).validate(any());

    clickOn("Select or search for a location");
    write("1");
    clickOn("(1) Floor 1");

    clickOn("Delivery Time");
    write("00:30 AM");
//    clickOn("Submit");
//    verify(validator, times(6)).validate(any());

    clickOn("First Name On Credit Card");
    write("FirstTest");
//    clickOn("Submit");
//    verify(validator, times(7)).validate(any());

    clickOn("Last Name On Card");
    write("LastTest");
//    clickOn("Submit");
//    verify(validator, times(8)).validate(any());

    clickOn("Type of Card");
    clickOn("Visa");
//    clickOn("Submit");
//    verify(validator, times(9)).validate(any());

    clickOn("Credit Card Number");
    write("1234567891234564");
//    clickOn("Submit");
//    verify(validator, times(10)).validate(any());

    clickOn("MM");
    clickOn("06");
//    clickOn("Submit");
//    verify(validator, times(11)).validate(any());

    clickOn("YYYY");
    clickOn("2023");
//    clickOn("Submit");
//    verify(validator, times(12)).validate(any());

    clickOn("CCV");
    write("456");
//    clickOn("Submit");
//    verify(validator, times(13)).validate(any());
//
    clickOn("Email Address for receipt");
    write("asdf@asdfff.com");
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
//
    verifyThat("Total: $9.99", javafx.scene.Node::isVisible);

    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Gift"), eq("Sender Name"),
        eq(new GiftDeliveryRequestData("Stuffed Animal", "Getter Name")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());


  }

  @Test
  public void verifySubmit() {
    verifyThat("Submit", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSubmit() throws IOException {
    when(validator.validate(any())).thenReturn(false).thenReturn(true).thenReturn(true);
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn(null).thenReturn("ABCDEFGH");

    // Test when there are fields not filled out
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(1)).showBasic(any(), any(), any());

    // Test when there are fields filled out (but adding fails)
    clickOn("Recipient Name");
    write("John Smith");
    clickOn("Sender Name");
    write("Sender Name");
    clickOn("Items");
    clickOn("Toy: $3.99");
    clickOn("Select or search for a location");
    clickOn("(5) Floor 5");
    clickOn("Delivery Time");
    write("12:12 AM");
    clickOn("First Name On Credit Card");
    write("First");
    clickOn("Last Name On Card");
    write("Last");
    clickOn("Type of Card");
    clickOn("AMEX");
    clickOn("Credit Card Number");
    write("1234567891234567");
    clickOn("MM");
    clickOn("10");
    clickOn("YYYY");
    clickOn("2020");
    clickOn("CCV");
    write("999");
    clickOn("Email Address for receipt");
    write("asdf@sdfggdfgsdfg.com");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 5"), eq("Gift"), eq("Sender Name"),
        eq(new GiftDeliveryRequestData("Toy", "John Smith")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1)).showBasic(any(), any(), any());

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(2)).addServiceRequest(anyString(),
        eq("Floor 5"), eq("Gift"), eq("Sender Name"),
        eq(new GiftDeliveryRequestData("Toy", "John Smith")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(2)).showBasic(any(), any(), any());
  }
}

  /**
   * Test List Items - 2 Recipient Name - 3 Sender Name - 3 Select Floor - 2 Select Room - 3
   * Delivery Time - 3 First Name - 3 Last Name - 3 Type - 3 CC Num - 3 MM - 3 YYYY - 3 CCV - 3
   * Email - 4 Submit - 8 1. Nothing 2. Everything 3. Some 4. Misisng half 5. Missing other half 6.
   * Missing Boxes 7. Missing Fields 8. Only time, email, cc, ccv
   */


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


