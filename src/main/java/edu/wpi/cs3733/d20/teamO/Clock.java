package edu.wpi.cs3733.d20.teamO;


import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Clock extends ViewModelBase {

  @FXML
  private Label time, date;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    date.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    Timeline clock = new Timeline(
        new KeyFrame(Duration.ZERO, e -> {
          LocalTime currentTime = LocalTime.now();
//          currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

          time.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        }),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

}



