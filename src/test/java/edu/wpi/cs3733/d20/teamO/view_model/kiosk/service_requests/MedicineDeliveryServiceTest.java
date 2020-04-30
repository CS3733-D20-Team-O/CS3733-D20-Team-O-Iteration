package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXDialog;
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

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class MedicineDeliveryServiceTest extends FxRobot {

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
  JFXDialog jfxDialog;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();
  @InjectMocks
  MedicineDeliveryService viewModel;

  private void initializeBundle() {
    //General use Bundles
    bundle.put("floorPrompt", "Floor");
    bundle.put("floorPromptValidator", "A Floor is Required for the Service Request!");
    bundle.put("locationPrompt", "Room/Location on Floor");
    bundle.put("locationPromptValidator", "A Room or Location is Required for the Service Request!");
    bundle.put("timePrompt", "Time for Request");
    bundle.put("notesPrompt", "Additional Notes:");
    bundle.put("submitButton", "Submit");
    bundle.put("cancelButton", "Cancel");

    //Unique Bundles
    bundle.put("serviceMedicineDeliveryDescription", "Medicine Delivery Service Request");
    bundle.put("serviceMedicineDeliveryPatientNameField", "Patient name");
    bundle.put("serviceMedicineDeliveryPatientNameFieldValidator", "Patient Name is Required!");
    bundle.put("serviceMedicineDeliveryMedicationNameField", "Medicine name");
    bundle.put("serviceMedicineDeliveryMedicationNameFieldValidator", "Medicine Name is Required!");
    bundle.put("serviceMedicineDeliveryDeliveryMethod", "Delivery method");
    bundle.put("serviceMedicineDeliveryDeliveryMethodValidator", "Delivery Method is Required!");

  }

  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    initializeBundle();
    populateFloorAndLocation();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/MedicineDeliveryService.fxml"))));
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

  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Floor");
    verifyThat("1", javafx.scene.Node::isVisible);
    verifyThat("3", javafx.scene.Node::isVisible);
    verifyThat("5",
        javafx.scene.Node::isVisible);    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("1");
    clickOn("Room/Location on Floor");
    verifyThat("Floor 1", javafx.scene.Node::isVisible);    // Third floor
    clickOn("1");
    clickOn("3");
    clickOn("Room/Location on Floor");
    verifyThat("Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("Floor 3-2", javafx.scene.Node::isVisible);    // Fifth floor
    clickOn("3");
    clickOn("5");
    clickOn("Room/Location on Floor");
    verifyThat("Floor 5", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSubmit1() {
    // Test when there are fields not filled out
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());
  }

  @Test
  public void testSubmit2() {
    // Test when there are fields filled out (but adding fails)
    clickOn("Patient name");
    write("John Smith");
    clickOn("Floor");
    clickOn("1");
    clickOn("Room/Location on Floor");
    clickOn("Floor 1");
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
  }

  @Test
  public void testSubmit3() throws IOException {
    // todo finish this test case
    // Test successful submission
    clickOn("Patient name");
    write("John Smith");
    clickOn("Medicine name");
    write("Ibuprofen");
    clickOn("Floor");
    clickOn("1");
    clickOn("Room/Location on Floor");
    clickOn("Floor 1");
    clickOn("Delivery method");
    clickOn("Oral");
    clickOn("Time for Request");
    write("02:00 AM");
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    /*
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Medicine delivery"), eq("John Smith"),
        eq(new MedicineDeliveryServiceData("Ibuprofen", "Oral")));
    //verify(snackBar, times(1)).show(anyString());
    //verify(dialog, times(1)).showFullscreenFXML(anyString());
    //verify(jfxDialog, times(1)).close();

     */
  }
}
