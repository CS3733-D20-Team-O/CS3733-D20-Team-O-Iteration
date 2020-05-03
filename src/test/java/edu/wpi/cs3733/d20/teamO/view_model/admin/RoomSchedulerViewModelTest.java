package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class RoomSchedulerViewModelTest extends FxRobot {

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
  RequestConfirmationViewModel requestConfirmationViewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @InjectMocks
  RoomSchedulerViewModel viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/RoomScheduler.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void checkIfJFXTextFieldIsEmpty() {
    JFXTextField tf = new JFXTextField();
    assertTrue(tf.getText().isEmpty());
  }

  @Test
  public void verifyIncrementButton() {
    verifyThat(">", javafx.scene.Node::isVisible);
  }

  @Test
  public void verifyOnCallBed() {
    verifyThat("On Call Bed", javafx.scene.Node::isVisible);
  }

  @Test
  public void testIncrementButton() {
    verifyThat("0", javafx.scene.Node::isVisible);
    clickOn(">");
    verifyThat("1", javafx.scene.Node::isVisible);
  }

  @Test
  public void testDecrementButton() {
    verifyThat("0", javafx.scene.Node::isVisible);
    clickOn("<");
    verifyThat("0", javafx.scene.Node::isVisible);
  }
}