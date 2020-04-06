package edu.wpi.onyx_ouroboros;

import edu.wpi.onyx_ouroboros.model.language.languages.EnglishModel;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.greenrobot.eventbus.EventBus;

@Slf4j
public class Main extends Application {

  public static void main(String[] args) {
    // Set English as default language
    EventBus.getDefault().postSticky(new EnglishModel());
    launch(args);
  }

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("views/Main.fxml"));
    final Parent root = loader.load();
    val viewModel = loader.getController();
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
