package edu.wpi.cs3733.d20.teamO;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
    EventHandler handler = new EventHandler() {
      @Override
      public void handle(Event event) {
        resetTimer();
        event.consume();
      }
    };

    //todo add more listeners for other possible types of user interaction
    root.addEventHandler(MouseEvent.ANY, handler);
    root.addEventHandler(KeyEvent.ANY, handler);
    root.addEventHandler(ScrollEvent.ANY, handler);

    System.out.println("start of IdleDetector finished");
  }

  public void resetTimer() {
    //set new time-out timer
    timeline = new Timeline(new KeyFrame(
        Duration.millis(2500),
        ae -> timeOut()));
    //start the time-out timer
    timeline.play();

    System.out.println("Timer reset to 2500 ms");
  }

  private void timeOut() {
    System.out.println("Attempting to time-out");
    try {
      navigator.empty();
    } catch (IOException e) {
      System.out.println(
          "IOException encountered when attempting to call Navigator.empty() on time-out.");
    }
    //todo do something here, maybe call resetTimer again? maybe do nothing until the user inputs again?
  }
}
