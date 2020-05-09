package edu.wpi.cs3733.d20.teamO.model.datatypes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Represents login details passed in via the command line
 */
@Data
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class LoginDetails {

  private final DatabaseWrapper database;
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
    for (val e : database.exportEmployees()) {
      if (e.getEmployeeID().equals(username)) {
        if (e.getPassword().equals(password)) {
          return true;
        }
      }
    }
    return false;
  }
}
