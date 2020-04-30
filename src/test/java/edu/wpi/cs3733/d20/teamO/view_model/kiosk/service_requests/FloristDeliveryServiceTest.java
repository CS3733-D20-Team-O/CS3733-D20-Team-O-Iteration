package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
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
class FloristDeliveryServiceTest extends FxRobot {

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
  FloristDeliveryService viewModel;

  private void initializeBundle() {
    //General use Bundles
    bundle.put("serviceFloristDeliveryDescription ", "Florist Delivery Service Request");
    bundle.put("namePrompt", "Your Name");
    bundle.put("namePromptValidator", "Your name is required!");
    bundle.put("floorPrompt", "Floor");
    bundle.put("floorPromptValidator", "A Floor is Required for the Service Request!");
    bundle.put("locationPrompt", "Room/Location on Floor");
    bundle.put("locationPromptValidator", "A Room or Location is Required for the Service Request!");
    bundle.put("submitButton", "Submit");
    bundle.put("cancelButton", "Cancel");

    //Unique Bundles
    bundle.put("BouquetType", "Bouquet Type");
    bundle.put("BouquetValidator", "You need to Select a Bouquet!");
  }


  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    populateFloorAndLocation();
    initializeBundle();
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/FloristDeliveryService.fxml"))));
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
  public void testFloorLocationPopulated() {
    // Verify that all floors are populated
    clickOn("Floor");
    verifyThat("1", javafx.scene.Node::isVisible);
    verifyThat("3", javafx.scene.Node::isVisible);
    verifyThat("5", javafx.scene.Node::isVisible);

    // Now that we know all floors are correct, lets check to see if the locations are present
    // First floor
    clickOn("1");
    verifyThat("1", javafx.scene.Node::isVisible);

    // Third floor
    clickOn("1");
    clickOn("3");
    clickOn("Floor 3-1");
    verifyThat("Floor 3-1", javafx.scene.Node::isVisible);
    verifyThat("Floor 3-2", javafx.scene.Node::isVisible);

    // Fifth floor
    clickOn("3");
    clickOn("5");
    verifyThat("5", javafx.scene.Node::isVisible);
  }

  @Test
  public void testItems1() {
    clickOn("Bouquet Type");
    verifyThat("Basket 9.99", javafx.scene.Node::isVisible);
    verifyThat("Biedermeier 7.99", javafx.scene.Node::isVisible);
    verifyThat("Cascade 19.99", javafx.scene.Node::isVisible);
    verifyThat("Nosegay 99.99", javafx.scene.Node::isVisible);
    verifyThat("Posy 28.99", javafx.scene.Node::isVisible);
    verifyThat("Presentation 49.99", javafx.scene.Node::isVisible);
    verifyThat("Round 9.99", javafx.scene.Node::isVisible);
    verifyThat("Single Stem 38.99", javafx.scene.Node::isVisible);
    verifyThat("Shower 3.99", javafx.scene.Node::isVisible);

    clickOn("Pomander 39.99");
    verifyThat("Pomander 39.99", javafx.scene.Node::isVisible);
  }

  @Test
  public void testItems2() {
    clickOn("Bouquet Type");
    verifyThat("Basket 9.99", javafx.scene.Node::isVisible);
    verifyThat("Biedermeier 7.99", javafx.scene.Node::isVisible);
    verifyThat("Cascade 19.99", javafx.scene.Node::isVisible);
    verifyThat("Nosegay 99.99", javafx.scene.Node::isVisible);
    verifyThat("Posy 28.99", javafx.scene.Node::isVisible);
    verifyThat("Presentation 49.99", javafx.scene.Node::isVisible);
    verifyThat("Round 9.99", javafx.scene.Node::isVisible);
    verifyThat("Single Stem 38.99", javafx.scene.Node::isVisible);
    verifyThat("Shower 3.99", javafx.scene.Node::isVisible);

    clickOn("Shower 3.99");
    verifyThat("Shower 3.99", javafx.scene.Node::isVisible);
  }

  @Test
  public void toFieldEmpty() {
    clickOn("Bouquet Type");
    clickOn("Cascade 19.99");
    clickOn("Submit");
    verify(validator, times(1)).validate(any());

    clickOn("Your Name");
    write("Getter Name");
    clickOn("Submit");
    verify(validator, times(2)).validate(any());

    clickOn("Floor");
    clickOn("3");
    clickOn("Submit");
    verify(validator, times(3)).validate(any());

    clickOn("Floor 3-1");
    clickOn("Floor 3-1");
    clickOn("Submit");
    verify(validator, times(4)).validate(any());

  }

}