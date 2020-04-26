package edu.wpi.cs3733.d20.teamO.model.material;

import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.Navigator;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
public class SnackBarTest extends FxRobot {

  @Spy
  StackPane root;
  @Mock
  Navigator navigator;

  @InjectMocks
  SnackBar snackBar;

  @Start
  public void start(Stage stage) {
    root = new StackPane();
    when(navigator.getRoot()).thenReturn(root);
    stage.setScene(new Scene(root));
    stage.setAlwaysOnTop(true);
    stage.show();
    snackBar.show("Test Passed");
  }

  @Test
  public void testShow() {
    verifyThat("Test Passed", Node::isVisible);
  }
}
