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
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.AVRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.io.IOException;
import java.time.LocalDateTime;
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
public class AVServiceTest extends FxRobot {

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
  AVService viewModel;

  private void bundleInit() {
    bundle.put("serviceAVRequestCreation", "Audio/Visual Service Request");
    bundle.put("serviceAVRequesterNameField", "Your Name");
    bundle.put("serviceAVFloorNumberComboBox", "Floor Number");
    bundle.put("serviceAVLocationComboBox", "Location");
    bundle.put("serviceAVRequestComboBox", "Select a service");
    bundle.put("serviceAVStartTimePicker", "At what time?");
    bundle.put("serviceAVDurationComboBox", "For how long?");
    bundle.put("serviceAVCommentTextArea", "Additional Notes");
    bundle.put("serviceAVSubmitButton", "Submit");
    bundle.put("serviceAVCancelButton", "Cancel");
  }

  @Start
  public void start(Stage stage) throws IOException {
    bundleInit();
    populateFloorAndLocation();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/AVService.fxml"))));
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

  /*
  @Test
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Floor Number");
    sleep(2000);
    verifyThat("1", javafx.scene.Node::isVisible);
    verifyThat("3", javafx.scene.Node::isVisible);
    verifyThat("5", javafx.scene.Node::isVisible);

    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("Floor 1");
    verifyThat("Floor 1", javafx.scene.Node::isVisible);

    // Third floor
    clickOn("1");
    clickOn("3");
    clickOn("Floor 3-1");
    verifyThat("Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("Floor 3-2", javafx.scene.Node::isVisible);

    // Fifth floor
    clickOn("3");
    clickOn("5");
    verifyThat("Floor 5", javafx.scene.Node::isVisible);
  }
  */

  @Test
  public void testComboBox() {
    clickOn("Select a service");
    verifyThat("Music", javafx.scene.Node::isVisible);
    verifyThat("Video Call", javafx.scene.Node::isVisible);
  }

  @Test
  public void testTextFields() {
    clickOn("Your Name");
    write("Alan Smithee");
    verifyThat("Alan Smithee", javafx.scene.Node::isVisible);
    clickOn("Additional Notes");
    write("Sample Text");
    verifyThat("Sample Text", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSubmit() {
    populateFloorAndLocation();
    when(validator.validate(any())).thenReturn(false).thenReturn(true).thenReturn(true);
    when(database.addServiceRequest(any(), any(), any(), any(), any()))
        .thenReturn(null).thenReturn("Y4NK3EW1THN0BR1M");

    // Test when there are fields not filled out
    clickOn("Submit");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).addServiceRequest(any(), any(), any(), any(), any());
    verify(snackBar, times(0)).show(anyString());
    verify(dialog, times(1)).showBasic(any(), any(), any());

    // Test when there are fields filled out (but adding fails)
    clickOn("Your Name");
    write("Alan Smithee");
    clickOn("Floor Number");
    clickOn("1");
    clickOn("Location");
    clickOn("Floor 1");
    clickOn("Select a service");
    clickOn("Music");
    clickOn("At what time?");
    write("4:20 PM");
    clickOn("For how long?");
    clickOn("10 minutes");
    clickOn("Additional Notes");
    write("Sample Text");

    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(0)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("AV"), eq("Alan Smithee"),
        eq(new AVRequestData("Music", "Sample Text", "10 minutes",
            LocalDateTime.now().toString())));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1)).showBasic(any(), any(), any());

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(0)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("AV"), eq("Alan Smithee"),
        eq(new AVRequestData("Music", "Sample Text", "10 minutes",
            LocalDateTime.now().toString())));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(2)).showBasic(anyString(), anyString(), anyString());
  }
}
