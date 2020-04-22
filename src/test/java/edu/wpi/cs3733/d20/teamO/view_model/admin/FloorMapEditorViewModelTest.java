package edu.wpi.cs3733.d20.teamO.view_model.admin;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
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

/**
 * Tests FloorMapEditor
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class FloorMapEditorViewModelTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  NodeMapView mapView;

  @InjectMocks
  FloorMapEditorViewModel viewModel;

  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);

    stage.setScene(new Scene(loader.load(Main.class.getResourceAsStream("views/admin/FloorMapEditor.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  void testNodeMapFloorSelect() {

  }

  @Test
  void testNodeMapDrawNodeEdge() {

  }
}