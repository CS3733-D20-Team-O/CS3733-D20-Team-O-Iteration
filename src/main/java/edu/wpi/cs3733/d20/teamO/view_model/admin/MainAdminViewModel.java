package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainAdminViewModel extends Dialog.DialogViewModel {

  private final Navigator navigator;
  private final Dialog dialog;
  private final FXMLLoader fxmlLoader;
  private final SelectedPathFinder selectedPathFinder;
  private final LoginDetails loginDetails;

  @FXML
  private JFXComboBox<Label> searchAlgorithms;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the path finders here that are in the FXML
    val pathFinders = selectedPathFinder.getPathFinders();

    // Preselect the option currently in use
    for (int i = 0; i < pathFinders.length; ++i) {
      val currentlySelectedPathFinder = selectedPathFinder.getCurrentPathFinder().getClass();
      val pathFinder = pathFinders[i].getClass();
      if (pathFinder.equals(currentlySelectedPathFinder)) {
        searchAlgorithms.getSelectionModel().select(i);
        break;
      }
    }

    // Set a listener to set the algorithm to use
    searchAlgorithms.getSelectionModel().selectedIndexProperty().addListener((o, oldInt, newInt) ->
        selectedPathFinder.setCurrentPathFinder(pathFinders[newInt.intValue()]));
  }

  @FXML
  private void openFloorMapEditor() {
    try {
      navigator.push("Floor Map Editor", "views/admin/FloorMapEditor.fxml");
    } catch (IOException e) {
      log.error("Failed to open the floor map editor", e);
    }
  }

  @FXML
  private void openServiceRequestHandler() {
    try {
      navigator.push("Service Request Handler", "views/admin/RequestHandler.fxml");
    } catch (IOException e) {
      log.error("Failed to open the service request handler", e);
    }
  }

  @FXML
  private void openImportExportHandler() {
    try {
      // Clear any previously usage of the fxmlLoader
      fxmlLoader.setRoot(null);
      fxmlLoader.setController(null);
      // Show the dialog in full screen mode
      val displayedDialog = dialog.showFullscreen(fxmlLoader.load(Main.class
          .getResourceAsStream("views/admin/ImportExportCSV.fxml")));
      // Connect the dialog's back button to the actual dialog via setDialog()
      ((ImportExportCSVViewModel) fxmlLoader.getController()).setDialog(displayedDialog);
    } catch (IOException e) {
      log.error("Failed to open the import/export csv dialog", e);
    }
  }

  @FXML
  private void logout(ActionEvent actionEvent) {
    // todo set login etails
    close();
  }
}
