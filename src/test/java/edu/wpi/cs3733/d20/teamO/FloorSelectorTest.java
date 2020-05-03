package edu.wpi.cs3733.d20.teamO;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.view_model.FloorSelector;
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
public class FloorSelectorTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @InjectMocks
  FloorSelector floorSelector;

  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> floorSelector);
    stage.setScene(
        new Scene(loader.load(Main.class.getResourceAsStream("views/FloorSelector.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testSelectFloors() {
    clickOn("1");
    verifyThat("1", javafx.scene.Node::isDisabled);
  }
}
