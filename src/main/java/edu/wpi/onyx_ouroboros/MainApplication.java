package edu.wpi.onyx_ouroboros;

import edu.wpi.onyx_ouroboros.view_model.MainViewModel;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class MainApplication extends ApplicationBase {

  @Override
  public void start(Stage primaryStage) throws IOException {
    val loader = create(FXMLLoader.class);
    val root = (Parent) loader.load(getClass().getResourceAsStream("views/Main.fxml"));
    val viewModel = (MainViewModel) loader.getController();
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
