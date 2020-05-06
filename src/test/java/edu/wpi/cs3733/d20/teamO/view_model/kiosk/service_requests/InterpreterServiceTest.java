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
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InterpreterData;
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
public class InterpreterServiceTest extends FxRobot {

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
  InterpreterService viewModel;

  private void initializeBundle() {
    bundle.put("serviceInterpreterDescription", "Interpreter Service Request Creation");
    bundle.put("serviceInterpreterNameField", "Your Name");
    bundle.put("serviceInterpreterNameValidator", "Your name is required!");
    bundle.put("nodeSelectorPromptText", "Select or search for a location");
    bundle.put("serviceInterpreterLocationValidator", "A location is required!");
    bundle.put("serviceInterpreterTimePicker", "Time for Request");
    bundle.put("serviceInterpreterTimeValidator", "You need to select a time for the interpreter!");
    bundle.put("serviceInterpreterLanguageCB", "Language");
    bundle.put("serviceInterpreterLanguageValidator",
        "You need to select the language for the interpreter!");
    bundle.put("serviceInterpreterGenderCB", "Preferred Gender");
    bundle.put("serviceInterpreterGenderValidator",
        "You need to select a preferred gender for the interpreter!");
    bundle.put("serviceInterpreterNotesField", "Additional Notes");
    bundle.put("serviceInterpreterSubmitButton", "Submit");
    bundle.put("serviceInterpreterCancelButton", "Cancel");
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
        .getResourceAsStream("views/kiosk/service_requests/InterpreterService.fxml"))));
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
  public void testLanguages() throws IOException {
    clickOn("Language");
    verifyThat("Spanish", javafx.scene.Node::isVisible);
    verifyThat("French", javafx.scene.Node::isVisible);
    verifyThat("Chinese", javafx.scene.Node::isVisible);
    verifyThat("Japanese", javafx.scene.Node::isVisible);
    verifyThat("Russian", javafx.scene.Node::isVisible);

    clickOn("Spanish");
    verifyThat("Spanish", javafx.scene.Node::isVisible);
  }

  @Test
  public void testGender() throws IOException {
    clickOn("Preferred Gender");
    verifyThat("Male", javafx.scene.Node::isVisible);
    verifyThat("Female", javafx.scene.Node::isVisible);
    verifyThat("No Preference", javafx.scene.Node::isVisible);

    clickOn("No Preference");
    verifyThat("No Preference", javafx.scene.Node::isVisible);
  }

  @Test
  public void everythingEmpty() {
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
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
    clickOn("Time for Request");
    write("12:24 PM");
    clickOn("Language");
    clickOn("Chinese");
    clickOn("Preferred Gender");
    clickOn("Male");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Interpreter"), eq("John Smith"),
        eq(new InterpreterData("Chinese", "Male", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());
    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(2)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Interpreter"), eq("John Smith"),
        eq(new InterpreterData("Chinese", "Male", "")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(anyString());
    verify(jfxDialog, times(1)).close();
  }
}