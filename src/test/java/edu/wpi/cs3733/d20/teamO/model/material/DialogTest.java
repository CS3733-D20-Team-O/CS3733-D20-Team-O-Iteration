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
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class DialogTest {

  @Spy
  StackPane root, testContent;
  @Mock
  Navigator navigator;

  @InjectMocks
  Dialog dialog;

  @Start
  public void start(Stage stage) {
    root = new StackPane();
    testContent = new StackPane();
    when(navigator.getRoot()).thenReturn(root);
    stage.setScene(new Scene(root));
    stage.setAlwaysOnTop(true);
    stage.show();
    dialog.showBasic("Test 1", "Test 2", "Test 3");
    dialog.showFullscreen(testContent);
  }

  @Test
  public void testShowBasic() {
    verifyThat("Test 1", Node::isVisible);
    verifyThat("Test 2", Node::isVisible);
    verifyThat("Test 3", Node::isVisible);
  }

  @Test
  public void testShowFullscreen() {
    verifyThat(testContent, Node::isVisible);
  }
}
