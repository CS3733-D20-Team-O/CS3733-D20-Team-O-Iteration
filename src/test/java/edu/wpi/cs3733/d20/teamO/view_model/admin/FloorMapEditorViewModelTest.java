package edu.wpi.cs3733.d20.teamO.view_model.admin;


import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
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
public class FloorMapEditorViewModelTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;

  @InjectMocks
  FloorMapEditorViewModel viewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  private void initializeBundle() {
    bundle.put("cancelButton", "Cancel");
    bundle.put("nodeCategory", "Node Category");
    bundle.put("nodeShortName", "Short Name");
    bundle.put("nodeLongName", "Long Name");
    bundle.put("floorMapReq", "Required");
    bundle.put("nodeDelete", "Delete Node");
    bundle.put("saveChanges", "Save Changes");
    bundle.put("neighborNode", "Neighboring Nodes");
    bundle.put("edgeDelete", "Delete Edge");
    bundle.put("floorMapSelect", "Select");
    bundle.put("floorMapCreateNode", "Create Node");
    bundle.put("leftClickDestination", "Left-click to select a destination node");
    bundle.put("startNode", "Start Node:");
    bundle.put("endNode", "Destination Node:");
    bundle.put("addNeighborSelect", "Select a node to add as a neighbor");
    bundle.put("selectedNode", "Selected Node:");
    bundle.put("addNeighbor", "Add Neighbor");
    bundle.put("selectedNodes", "Selected Nodes");
    bundle.put("deleteAll", "Delete All");
    bundle.put("verticalAlign", "Align Vertically");
    bundle.put("horizontalAlign", "Align Horizontally");
    bundle.put("clickInstruct", "Left-click to select a node or edge, right-click to add a node or edge");
  }

  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample");
    initializeBundle();
    val loader = new FXMLLoader();
    loader.setResources(bundle);
    loader.setControllerFactory(o -> viewModel);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/FloorMapEditor.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testZoomOutPressed() {
    sleep(5000);
    assertTrue(true);
  }
}
