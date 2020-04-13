package edu.wpi.onyx_ouroboros;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class AdminApplication extends ApplicationBase {

  @Override
  public void start(Stage primaryStage) throws IOException {
    val loader = new FXMLLoader(getClass().getResource("views/Main.fxml"));
    val root = (Parent) loader.load();
    val viewModel = loader.getController();
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
