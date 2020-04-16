package edu.wpi.cs3733.d20.teamO;

import edu.wpi.cs3733.d20.teamO.view_model.main_screen.AdminViewModel;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Value;
import lombok.val;

public class AdminApplication extends ApplicationBase {

  @Override
  public void start(Stage primaryStage) throws IOException {
    val loader = new FXMLLoader();
    loader.setController(create(AdminViewModel.class));
    val root = (Parent) loader.load(getClass().getResourceAsStream("views/Main.fxml"));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  /**
   * @return the login details used to launch this application
   */
  private LoginDetails getLoginDetails() {
    return new LoginDetails(getParameters().getRaw());
  }

  /**
   * Represents login details passed in via the command line
   */
  @Value
  static class LoginDetails {

    String username, password;

    LoginDetails(List<String> args) {
      String username = null, password = null;
      for (val arg : args) {
        if (arg.startsWith("--username=")) {
          username = arg.substring(11);
        } else if (arg.startsWith("--password=")) {
          password = arg.substring(11);
        }
      }
      this.username = username;
      this.password = password;
    }

    boolean isValid() {
      return username != null && password != null && !username.isBlank() && !password.isBlank();
    }
  }
}
