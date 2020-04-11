package edu.wpi.onyx_ouroboros;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class PrototypeApplication extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    // todo load all 4 windows from views/prototype/*.fxml
    val loader = new FXMLLoader(getClass().getResource("views/Main.fxml"));
    val root = (Parent) loader.load();
    val viewModel = loader.getController();
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
