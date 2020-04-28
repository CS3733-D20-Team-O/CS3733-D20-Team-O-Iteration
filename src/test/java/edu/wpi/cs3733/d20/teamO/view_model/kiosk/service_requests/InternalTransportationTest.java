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
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
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
public class InternalTransportationTest extends FxRobot {

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
  InternalTransportationService viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    populateFloorAndLocation();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/InternalTransportationService.fxml"))));
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
    clickOn("Requester Name");
    write("John Smith");
    clickOn("Request Time");
    write("12:43");
    clickOn("Unassisted Transportation");
    clickOn("Floor 1");
    clickOn("Floor 1");
    clickOn("Crutches");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());
    verify(database, times(1)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Int. Transport"), eq("John Smith"),
        eq(new InternalTransportationRequestData("Unassisted", "Crutches", "None required")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(0)).showBasic(any(), any(), any());

    // Test when there are fields filled out (and adding succeeds)
    clickOn("Submit");
    verify(validator, times(3)).validate(any());
    verify(database, times(2)).addServiceRequest(anyString(),
        eq("Floor 1"), eq("Int. Transport"), eq("John Smith"),
        eq(new InternalTransportationRequestData("Unassisted", "Crutches", "None required")));
    verify(snackBar, times(1)).show(anyString());
    verify(dialog, times(1)).showFullscreenFXML(anyString());
    verify(jfxDialog, times(1)).close();
  }

}
