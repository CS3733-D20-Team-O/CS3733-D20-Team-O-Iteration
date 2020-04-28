package edu.wpi.cs3733.d20.teamO.model.datatypes;

import com.google.inject.Singleton;
import java.util.List;
import lombok.Data;
import lombok.val;

/**
 * Represents login details passed in via the command line
 */
@Data
@Singleton
public class LoginDetails {

  private String username = "", password = "";

  public void setFromParameters(List<String> args) {
    for (val arg : args) {
      if (arg.startsWith("--username=")) {
        username = arg.substring(11);
      } else if (arg.startsWith("--password=")) {
        password = arg.substring(11);
      }
    }
  }

  public void reset() {
    username = password = "";
  }

  public boolean isValid() {
    return username.equals("staff") && password.equals("staff");
  }
}
