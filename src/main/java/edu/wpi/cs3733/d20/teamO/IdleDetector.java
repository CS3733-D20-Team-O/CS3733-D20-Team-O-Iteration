package edu.wpi.cs3733.d20.teamO;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdleDetector {

  Timeline timeline;
  StackPane root;
  Navigator navigator;

  IdleDetector(StackPane root, Navigator navigator) {
    this.root = root;
    this.navigator = navigator;
  }

  protected void start() {
    //set listeners for user interaction
    //todo add more listeners for other possible types of user interaction
    root.setOnKeyPressed(e -> resetTimer());
    root.setOnKeyReleased(e -> resetTimer());
    root.setOnMouseMoved(e -> resetTimer());
    root.setOnMousePressed(e -> resetTimer());
    root.setOnMouseReleased(e -> resetTimer());
    root.setOnScrollFinished(e -> resetTimer());
    root.setOnScrollStarted(e -> resetTimer());
    root.setOnContextMenuRequested(e -> resetTimer());

    log.error("start of IdleDetector finished");
  }

  public void resetTimer() {
    //set new time-out timer
    timeline = new Timeline(new KeyFrame(
        Duration.millis(2500),
        ae -> timeOut()));
    //start the time-out timer
    timeline.play();

    log.error("Timer reset to 2500 ms");
  }

  private void timeOut() {
    log.error("Attempting to time-out");
    try {
      navigator.empty();
    } catch (IOException e) {
      log.error("IOException encountered when attempting to call Navigator.empty() on time-out.");
    }
    //todo do something here, maybe call resetTimer again? maybe do nothing until the user inputs again?
  }
}
