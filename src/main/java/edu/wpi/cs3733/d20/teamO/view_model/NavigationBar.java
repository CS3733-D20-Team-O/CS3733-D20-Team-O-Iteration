package edu.wpi.cs3733.d20.teamO.view_model;

import com.google.inject.Inject;
import edu.wpi.cs3733.d20.teamO.Clock;
import edu.wpi.cs3733.d20.teamO.Navigator;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NavigationBar extends ViewModelBase {

  private Clock clock;
  private final Navigator navigator;
  private final SimpleStringProperty title = new SimpleStringProperty("Invalid Title");

  @FXML
  private void onBackPressed() {
    try {
      navigator.pop();
    } catch (IOException e) {
      log.error("Failed to go back one page from " + title.get(), e);
    }
  }

  public String getTitle() {
    return title.get();
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public SimpleStringProperty titleProperty() {
    return title;
  }
}
