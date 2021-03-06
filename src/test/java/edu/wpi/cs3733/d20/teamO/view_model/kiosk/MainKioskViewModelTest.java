package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
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
 * Tests MainKioskViewModel todo rewrite
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class MainKioskViewModelTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  LanguageHandler languageHandler;
  @Mock
  Navigator navigator;
  @Mock
  SnackBar snackBar;
  @Mock
  Dialog dialog;
  @Mock
  LoginDetails loginDetails;
  @Mock
  RequestConfirmationViewModel requestConfirmationViewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  MainKioskViewModel viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    // Add the necessary strings to the bundle
    bundle.put("serviceRequestLookupSubmit", "Submit");
    bundle.put("serviceRequestConfirmationID", "Click me");
    bundle.put("serviceRequestLookupFail", "pass");

    // Load the actual test environment
    when(languageHandler.getCurrentLocale()).thenReturn(Locale.ENGLISH);
    when(loginDetails.isValid()).thenReturn(false); // we can't properly test this
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/Main.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testDialog() throws IOException {
    val serviceRequest = new ServiceRequest("ABCDEFGH", "", "node",
        "Sanitation", "Unassigned", "", "", "",
        new SanitationRequestData("Dry Spill", ""));
    when(database.exportServiceRequests()).thenReturn(Collections.singletonList(serviceRequest));
    when(dialog.showFullscreenFXML(anyString())).thenReturn(requestConfirmationViewModel);
    clickOn("Click me");
    write("ABCDEFGH");
    clickOn(viewModel.getLookupButton());
    verify(dialog, times(1)).showFullscreenFXML(any());
    verify(snackBar, times(0)).show(anyString());
  }

  @Test
  public void testSnackBar() {
    clickOn(viewModel.getLookupButton());
    verify(snackBar, times(1)).show("pass");
    verify(dialog, times(0)).showFullscreen(any());
  }
}
