package edu.wpi.cs3733.d20.teamO.model.datatypes;

import java.util.List;
import lombok.Value;
import lombok.val;

/**
 * Represents login details passed in via the command line
 */
@Value
public class LoginDetails {

  String username, password;

  public LoginDetails(List<String> args) {
    String username = "", password = "";
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

  public boolean isValid() {
    return !username.isBlank() && !password.isBlank();
  }
}
