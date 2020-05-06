package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SecurityRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
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
public class SecurityServiceTest extends FxRobot {

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
  RequestConfirmationViewModel requestConfirmationViewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  SecurityService viewModel;

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
        .getResourceAsStream("views/kiosk/service_requests/SecurityService.fxml"))));
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
  public void testSubmit() throws IOException {
    when(validator.validate(any())).thenReturn(false).thenReturn(true).thenReturn(true);
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn(null).thenReturn("ABCDEFGH");
    when(dialog.showFullscreenFXML(anyString())).thenReturn(requestConfirmationViewModel);

    // Test when there are fields not filled out
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());

    // Test when there are fields filled out (but adding fails)
    clickOn("Your Name");
    write("John Smith");
    clickOn("Select or search for a location");
    write("1");
    clickOn("(1) Floor 1");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Security"), eq("John Smith"),
        eq(new SecurityRequestData("Code pink: Amber alert/Code Adam: infant abduction", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(2)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Security"), eq("John Smith"),
        eq(new SecurityRequestData("Code pink: Amber alert/Code Adam: infant abduction", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(anyString());
    verify(jfxDialog, times(1)).close();

    //todo add more tests for different types of emergencies
  }
}
