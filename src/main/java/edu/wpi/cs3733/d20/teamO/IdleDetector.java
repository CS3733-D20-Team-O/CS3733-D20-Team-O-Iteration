package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class IdleDetector {

  private Timeline timeline;
  private int timeOutTime;
  private final StackPane root;
  private final Navigator navigator;
  private final LoginDetails loginDetails;

  @Inject
  IdleDetector(Injector injector, Navigator navigator) {
    this.navigator = navigator;
    this.root = navigator.getRoot();
    this.loginDetails = injector.getInstance(LoginDetails.class);

    setTimeOutTime(30);
    root.addEventHandler(InputEvent.ANY, e -> resetTimer());
  }

  //Assume timeline is already defined
  public void resetTimer() {
    //stop previous timer
    timeline.stop();
    //start the time-out timer
    timeline.playFromStart();
  }

  private void timeOut() {
    System.out.println("Attempting to time-out");
    try {
      if (loginDetails.isValid()) {
        loginDetails.reset();
      }
      navigator.empty();
    } catch (IOException e) {
      log.error(
          "IOException encountered when attempting to call Navigator.empty() on time-out.");
      //todo do something here, maybe call resetTimer again? maybe do nothing until the user inputs again?
    }
  }

  public void setTimeOutTime(int timeOutTime) {
    this.timeOutTime = timeOutTime;
    if (timeline != null) {
      timeline.stop();
    }
    timeline = new Timeline(new KeyFrame(
        Duration.seconds(timeOutTime),
        ae -> timeOut()));
    System.out.println("TimeOutTime changed to " + timeOutTime + ", resetting timer.");
    resetTimer();
  }

  public int getTimeOutTime() {
    return timeOutTime;
  }
}
