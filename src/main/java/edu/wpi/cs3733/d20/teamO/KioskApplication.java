package edu.wpi.cs3733.d20.teamO;

import edu.wpi.cs3733.d20.teamO.view_model.main_screen.KioskViewModel;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class KioskApplication extends ApplicationBase {

  @Override
  public void start(Stage primaryStage) throws IOException {
    val loader = new FXMLLoader();
    loader.setController(get(KioskViewModel.class));
    val root = (Parent) loader.load(getClass().getResourceAsStream("views/Main.fxml"));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
