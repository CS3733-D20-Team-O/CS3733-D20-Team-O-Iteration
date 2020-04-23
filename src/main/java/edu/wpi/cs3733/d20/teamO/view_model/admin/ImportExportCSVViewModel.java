package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ImportExportCSVViewModel extends ViewModelBase {

  private final CSVHandler csvHandler;

  @FXML
  private AnchorPane root;

  @FXML
  private JFXTextField nodesFilename, edgesFilename, employeesFilename, requestsFilename;

  private void showConfirmation(boolean didWork) {
    JFXSnackbar bar = new JFXSnackbar(root);
    val label = new Label(didWork ?
        "Operation completed successfully" :
        "Operation failed. Invalid filename?");
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    // Add 16 margin and 16 padding as per material design guidelines
    container.setStyle("-fx-background-color: #323232;  -fx-background-insets: 16");
    container.setPadding(new Insets(32)); // total padding, including margin
    bar.enqueue(new SnackbarEvent(container));
  }

  @FXML
  private void importNodes() {
    showConfirmation(csvHandler.importNodes(nodesFilename.getText()));
  }

  @FXML
  private void exportNodes() {
    showConfirmation(csvHandler.exportNodes(nodesFilename.getText()));
  }

  @FXML
  private void importEdges() {
    showConfirmation(csvHandler.importEdges(edgesFilename.getText()));
  }

  @FXML
  private void exportEdges() {
    showConfirmation(csvHandler.exportEdges(edgesFilename.getText()));
  }

  @FXML
  private void importEmployees() {
    showConfirmation(csvHandler.importEmployees(employeesFilename.getText()));
  }

  @FXML
  private void exportEmployees() {
    showConfirmation(csvHandler.exportEmployees(employeesFilename.getText()));
  }

  @FXML
  private void importRequests() {
    showConfirmation(csvHandler.importServiceRequests(requestsFilename.getText()));
  }

  @FXML
  private void exportRequests() {
    showConfirmation(csvHandler.exportServiceRequests(requestsFilename.getText()));
  }
}
