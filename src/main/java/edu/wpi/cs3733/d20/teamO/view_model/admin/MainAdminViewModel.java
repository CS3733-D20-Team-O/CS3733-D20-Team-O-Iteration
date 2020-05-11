package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.IdleDetector;
import edu.wpi.cs3733.d20.teamO.Navigator;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MainAdminViewModel extends Dialog.DialogViewModel {

  private final Navigator navigator;
  private final Dialog dialog;
  private final SelectedPathFinder selectedPathFinder;
  private IdleDetector idleDetector;
  private final LoginDetails loginDetails;

  @FXML
  private JFXComboBox<Label> searchAlgorithms;

  @FXML
  private Spinner timeOutTime;

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

    //get the idleDetector used by Navigator
    idleDetector = navigator.getIdleDetector();

    // Set a listener to set the time-out time
    timeOutTime.valueProperty().addListener((o, oldInt, newInt) ->
        idleDetector.setTimeOutTime((int) newInt));

    // Set min, max, and initial value
    timeOutTime
        .setValueFactory(new IntegerSpinnerValueFactory(30, 3600, idleDetector.getTimeOutTime()));
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
  private void openEmployeeHandler() {
    try {
      navigator.push("Employee Handler", "views/admin/EmployeeHandler.fxml");
    } catch (IOException e) {
      log.error("Failed to open the employee handler", e);
    }
  }

  @FXML
  private void openImportExportHandler() {
    try {
      dialog.showFullscreenFXML("views/admin/ImportExportCSV.fxml");
    } catch (IOException e) {
      log.error("Failed to open the import/export csv dialog", e);
    }
  }

  @FXML
  private void openChangePassword() {
    try {
      dialog.showFullscreenFXML("views/admin/ChangePassword.fxml");
    } catch (IOException e) {
      log.error("Failed to open the change password dialog", e);
    }
  }

  @Override
  protected void onClose() {
    log.info("Resetting login details");
    loginDetails.reset();
  }
}
