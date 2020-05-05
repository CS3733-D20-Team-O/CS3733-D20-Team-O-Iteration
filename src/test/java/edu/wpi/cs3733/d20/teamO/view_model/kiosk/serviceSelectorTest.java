package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests.AVService;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class serviceSelectorTest extends FxRobot {

  @Mock
  Dialog dialog;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  serviceSelector viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("nodeSelectorPromptText", "node selector");
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/serviceSelector.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testClick() throws IOException {
    //Tests AVService
    clickOn("#AVService");
    clickOn("#ExtTransport");
    clickOn("#intTransport");
    clickOn("#Florist");
    clickOn("#giftDelivery");
    clickOn("#medDelivery");
    clickOn("#sanitationService");
    clickOn("#interpreterService");
    clickOn("#infoTech");
    clickOn("#securityService");
    verify(dialog, times(10)).showFullscreenFXML(any());
  }
}