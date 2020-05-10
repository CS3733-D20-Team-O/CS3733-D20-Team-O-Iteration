package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.EmergencyReport;
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


@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class EmergencyReportTest extends FxRobot {

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
  @Mock
  LoginDetails loginDetails = null;
  @Mock
  RequestConfirmationViewModel requestConfirmationViewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  EmergencyReport viewModel;

  private void initializeBundle() {
    //General use Bundles
    bundle.put("serviceSecurityDescription ", "Emergency Security  Service Request");
    bundle.put("namePrompt", "Your Name");
    bundle.put("namePromptValidator", "Your name is required!");
    bundle.put("floorPrompt", "Floor");
    bundle.put("floorPromptValidator", "A Floor is Required for the Service Request!");
    bundle.put("locationPrompt", "Room/Location on Floor");
    bundle.put("locationPromptValidator", "A Room or Location is Required for the Service Request!");
    bundle.put("submitButton", "Submit");
    bundle.put("cancelButton", "Cancel");

    //Unique Bundles
    bundle.put("serviceSecurityDescription", "Emergency Security  Service Request");
    bundle.put("serviceSecurityTitle", "Security Request");
    bundle.put("securityRequesterName", "Your Name");
    bundle.put("securityRequesterNameValidation", "Your name is Required!");
    bundle.put("nodeSelectorPromptText", "Select or search for a location");
    bundle.put("securityRequestLocationValidation",
        "Please indicate a location for responder to be sent!");
    bundle.put("SecurityEmergencyType","Emergency Type");
    bundle.put("codePink","Code pink: Amber alert/Code Adam: infant abduction");
    bundle.put("codeGrey","Code Grey: Combative/Violent Individual");
    bundle.put("codeSilver","Code Silver: Armed Individual");
    bundle.put("codeRed","Code Red: Fire");
    bundle.put("extTriage","External triage: external disaster");
    bundle.put("intTriage","Internal triage: internal emergency");
    bundle.put("suic","Suicidal or Distraught Individual");
  }


  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample");
    populateFloorAndLocation();
    initializeBundle();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/EmergencyReport.fxml"))));
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
//    when(database.exportNodes().values()).thenReturn(map.values());
  }

  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Location");
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
    }
    write("1");
    verifyThat("(1) Floor 1", javafx.scene.Node::isVisible);
    clickOn("(1) Floor 1");

    // Third floor
    clickOn("(1) Floor 1");
    for (int i = 0; i < 8; i++) {
      press(KeyCode.BACK_SPACE);
      release(KeyCode.BACK_SPACE);
    }
    write("3");
    verifyThat("(3) Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("(3) Floor 3-2", javafx.scene.Node::isVisible);
    clickOn("(3) Floor 3-1");

    // Fifth floor
    clickOn("(3) Floor 3-1");
    for (int i = 0; i < 8; i++) {
      press(KeyCode.BACK_SPACE);
      release(KeyCode.BACK_SPACE);
    }
    write("5");
    verifyThat("(5) Floor 5", javafx.scene.Node::isVisible);
    clickOn("(5) Floor 5");
    verifyThat("(5) Floor 5", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSubmit1() throws IOException {
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn("ABCDEFGH");
    when(loginDetails.getUsername()).thenReturn("");
    when(dialog.showFullscreenFXML(anyString())).thenReturn(requestConfirmationViewModel);

    // Test when there are fields not filled out
    clickOn("Submit");
    verify(database, times(1)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(any());
    verify(loginDetails, times(1)).getUsername();
  }

  @Test
  public void testSubmit2() throws IOException {
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn("ABCDEFGH");
    when(loginDetails.getUsername()).thenReturn("");
    when(dialog.showFullscreenFXML(anyString())).thenReturn(requestConfirmationViewModel);

    // Test when there are fields filled out (but adding fails)
    clickOn("Name");
    write("Submit Name2");
    clickOn("Location");
    write("1");
    clickOn("(1) Floor 1");
    clickOn("Bomb Threat");
    clickOn("Submit");
    verify(database, times(1)).exportNodes();
    verify(database, times(1)).exportEmployees();
    verify(database, times(1)).exportServiceRequests();
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Security"), eq("Submit Name2"),
        eq(new SecurityRequestData("Bomb Threat", "no notes")));
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(any());
    verify(loginDetails, times(1)).getUsername();

  }

  @Test
  public void testSubmit3() throws IOException {
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn("ABCDEFGH");
    when(loginDetails.getUsername()).thenReturn("barf");
    when(dialog.showFullscreenFXML(anyString())).thenReturn(requestConfirmationViewModel);

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Bomb Threat");
    clickOn("Submit");
    verify(database, times(1)).exportEmployees();
    verify(database, times(1)).exportServiceRequests();
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Unknown Location"), eq("Security"), eq("barf"),
        eq(new SecurityRequestData("Bomb Threat", "no notes")));
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(anyString());
    verify(loginDetails, times(2)).getUsername();

  }
}
