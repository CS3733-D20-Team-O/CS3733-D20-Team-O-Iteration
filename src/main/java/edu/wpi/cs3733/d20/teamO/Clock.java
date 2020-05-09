package edu.wpi.cs3733.d20.teamO;


import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Clock {

  @FXML
  private Label time;

  @Override
  protected void start(URL url, ResourceBundle resources) {
    Timeline clock = new Timeline(
        new KeyFrame(Duration.ZERO, e -> {
          LocalTime currentTime = LocalTime.now();
          time.setText(
              currentTime.getHour() + ":"
                  + currentTime.getMinute());

        }),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

}



