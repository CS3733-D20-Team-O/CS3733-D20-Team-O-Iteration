package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ExternalTransportationRequestData;
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
public class ExternalTransportationServiceTest extends FxRobot {

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
  ExternalTransportationService viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    //add the necessary strings to the bundle
    bundle.put("externalTransportationNamePrompt", "Your Name");
    bundle.put("externalTransportationFloorPrompt", "Floor");
    bundle.put("externalTransportationLocationPrompt", "Current Room/Location");
    bundle.put("externalTransportationModePrompt", "Mode of Transportation");
    bundle.put("externalTransportationTimePrompt", "Time");
    bundle.put("externalTransportationDestinationPrompt", "Destination");
    bundle.put("externalTransportationNotes", "Additional Notes");
    bundle.put("externalTransportationSubmit", "Submit");

    populateFloorAndLocation();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/ExternalTransportationService.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  private void populateFloorAndLocation() {
    val map = new HashMap<String, Node>();
    map.put("a", new Node("a", 0, 0, 1, "", "", "Room 1", ""));
    map.put("b", new Node("b", 0, 0, 3, "", "", "Room 3-1", ""));
    map.put("c", new Node("c", 0, 0, 3, "", "", "Room 3-2", ""));
    map.put("d", new Node("d", 0, 0, 5, "", "", "Room 5", ""));
    when(database.exportNodes()).thenReturn(map);
  }

  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Floor");
    verifyThat("1", javafx.scene.Node::isVisible);
    verifyThat("3", javafx.scene.Node::isVisible);
    verifyThat("5", javafx.scene.Node::isVisible);

    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("1");
    clickOn("Current Room/Location");
    verifyThat("Room 1", javafx.scene.Node::isVisible);

    // Third floor
    clickOn("1");
    clickOn("3");
    clickOn("Current Room/Location");
    verifyThat("Room 3-1", javafx.scene.Node::isVisible);
    verifyThat("Room 3-2", javafx.scene.Node::isVisible);

    // Fifth floor
    clickOn("3");
    clickOn("5");
    clickOn("Current Room/Location");
    verifyThat("Room 5", javafx.scene.Node::isVisible);
  }

  @Test
  public void testTransportationTypePopulated() {
    // Verify that all modes of transportation are populated
    clickOn("Mode of Transportation");
    verifyThat("Emergency Air Ambulance", javafx.scene.Node::isVisible);
    verifyThat("Non-Emergency Ground Ambulance", javafx.scene.Node::isVisible);
    verifyThat("Medical Car and Wheelchair Van", javafx.scene.Node::isVisible);
    verifyThat("Local Ground Ambulance Emergency Medical Services", javafx.scene.Node::isVisible);
    verifyThat("Shuttle Service", javafx.scene.Node::isVisible);
  }

  @Test
  public void testNameAndNotes() {
    // Verify that text fields work
    clickOn("Your Name");
    write("John Smith");
    verifyThat("John Smith", javafx.scene.Node::isVisible);
    clickOn("Additional Notes");
    write("Test Note");
    verifyThat("Test Note", javafx.scene.Node::isVisible);
  }

  @Test
  public void testDestination() {
    // Verify that text fields work (not generalizable)
    clickOn("Destination");
    write("My Home");
    verifyThat("My Home", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSubmit() {
    when(validator.validate(any())).thenReturn(false).thenReturn(true).thenReturn(true);
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn(null).thenReturn("ABCDEFGH");

    // Test when there are fields not filled out
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());

    // Test when there are fields filled out (but adding fails)
    clickOn("Your Name");
    write("John Smith");
    clickOn("Floor");
    clickOn("1");
    clickOn("Current Room/Location");
    clickOn("Room 1");
    clickOn("Mode of Transportation");
    clickOn("Shuttle Service");
    clickOn("Time");
    write("12:34 PM");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(1)).addServiceRequest(eq("12:34"),
        eq("Room 1"), eq("External Transportation"), eq("John Smith"),
        eq(new ExternalTransportationRequestData("Shuttle Service", "", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(2)).addServiceRequest(eq("12:34"),
        eq("Room 1"), eq("External Transportation"), eq("John Smith"),
        eq(new ExternalTransportationRequestData("Shuttle Service", "", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1))
        .showBasic(anyString(), eq("Your confirmation code is:\nABCDEFGH"), anyString());
  }
}
