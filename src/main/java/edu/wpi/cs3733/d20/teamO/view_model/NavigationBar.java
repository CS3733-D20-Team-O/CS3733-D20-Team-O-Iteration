package edu.wpi.cs3733.d20.teamO.view_model;

import edu.wpi.cs3733.d20.teamO.events.BackwardNavigationEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class NavigationBar extends ViewModelBase {

  private final SimpleStringProperty title = new SimpleStringProperty("Invalid Title");

  @FXML
  private void onBackPressed() {
    dispatch(new BackwardNavigationEvent());
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
