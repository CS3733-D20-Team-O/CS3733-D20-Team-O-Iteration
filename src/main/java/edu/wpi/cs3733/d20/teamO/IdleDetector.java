package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdleDetector {

  private Timeline timeline;
  private final Injector injector;
  private final StackPane root;
  private final Navigator navigator;
  private final LoginDetails loginDetails;
  private final JFXDialog dialog;


  @Inject
  IdleDetector(Injector injector, StackPane root, JFXDialog dialog, Navigator navigator) {
    this.injector = injector;
    this.root = root;
    this.dialog = dialog;
    this.navigator = navigator;
    this.loginDetails = injector.getInstance(LoginDetails.class);

    root.addEventHandler(InputEvent.ANY, e -> resetTimer());
    dialog.addEventHandler(InputEvent.ANY, e -> resetTimer());
  }

  public void resetTimer() {
    //stop previous timer
    if (timeline != null) {
      timeline.stop();
    }
    //set new time-out timer
    timeline = new Timeline(new KeyFrame(
        Duration.millis(2500),
        ae -> timeOut()));
    //start the time-out timer
    timeline.play();

    System.out.println("Timer reset");
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
}
