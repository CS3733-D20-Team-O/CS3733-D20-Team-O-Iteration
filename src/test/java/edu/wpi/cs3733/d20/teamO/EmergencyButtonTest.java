package edu.wpi.cs3733.d20.teamO;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.view_model.EmergencyReport;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.RequestConfirmationViewModel;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class EmergencyButtonTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  RequestConfirmationViewModel requestConfirmationViewModel;
  @Mock
  EmergencyReport EmergencyReportViewModel;
  @Mock
  Dialog dialog;

  @InjectMocks
  EmergencyButton emergencyButton;
  @InjectMocks
  EmergencyReport emergencyReviewViewModel;


  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> emergencyButton);
    stage.setScene(
        new Scene(loader.load(Main.class.getResourceAsStream("views/EmergencyButton.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testText() {
    verifyThat("Emergency", javafx.scene.Node::isVisible);
  }

  @Test
  public void testPresS() throws IOException {
    when(dialog.showFullscreenFXML(anyString())).thenReturn(EmergencyReportViewModel);

    clickOn("Emergency");
    verify(dialog, times(1)).showFullscreenFXML(anyString());
  }
}