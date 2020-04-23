package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.LanguageHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
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
 * Tests MainKioskViewModel todo fix up
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class MainKioskViewModelTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  LanguageHandler languageHandler;

  @InjectMocks
  MainKioskViewModel viewModel;

  @Spy
  private final ResourceBundle bundle = new ResourceBundle() {
    @Override
    protected Object handleGetObject(String s) {
      if (s.equals("serviceRequestLookupSubmit")) {
        return "Submit";
      }
      if (s.equals("serviceRequestConfirmationID")) {
        return "Click me";
      }
      if (s.equals("serviceRequestLookupFail")) {
        return "pass";
      }
      return "";
    }

    @Override
    public boolean containsKey(String key) {
      return true;
    }

    @Override
    public Enumeration<String> getKeys() {
      return Collections.emptyEnumeration();
    }
  };

  // Sets up the stage for testing
  @Start
  public void start(Stage stage) throws IOException {
    when(languageHandler.getCurrentLocale()).thenReturn(Locale.ENGLISH);
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/Main.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testDialog() {
    val node = new Node("node", 0, 0, 1,
        "", "", "Long Name", "");
    val serviceRequest = new ServiceRequest("valid", "", "node",
        "Gift", "", "", "");
    when(database.exportNodes()).thenReturn(Collections.singletonMap(node.getNodeID(), node));
    when(database.exportServiceRequests()).thenReturn(Collections.singletonList(serviceRequest));
    clickOn("Click me");
    write("valid");
    clickOn(viewModel.getLookupButton());
    verifyThat("Gift Service Request", javafx.scene.Node::isVisible);
  }

  @Test
  public void testSnackBar() {
    clickOn(viewModel.getLookupButton());
    verifyThat("pass", javafx.scene.Node::isVisible);
  }

  public void serviceSelectionNavigatesToNewPage() {
    // todo
    // test dispatch
    // test service selection reset
  }
}
