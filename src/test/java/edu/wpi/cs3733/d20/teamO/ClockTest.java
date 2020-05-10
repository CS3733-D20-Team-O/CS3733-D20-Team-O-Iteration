package edu.wpi.cs3733.d20.teamO;


import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
class ClockTest extends FxRobot {

  @Mock
  EventBus eventBus;

  @InjectMocks
  Clock clockModel;


  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> clockModel);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/Clock.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testClock1() {
    clockModel.getClass();
  }

  @Test
  public void testClock2() {

    val nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    verifyThat(nowDate, javafx.scene.Node::isVisible);
  }

  @Test
  public void testClock3() {

    val nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    verifyThat(nowDate, javafx.scene.Node::isVisible);
  }

}