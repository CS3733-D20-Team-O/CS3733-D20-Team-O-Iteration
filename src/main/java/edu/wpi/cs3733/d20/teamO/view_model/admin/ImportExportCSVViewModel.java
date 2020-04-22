package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ImportExportCSVViewModel extends ViewModelBase {

  private final CSVHandler csvHandler;

  @FXML
  private JFXTextField nodesFilename, edgesFilename, employeesFilename, requestsFilename;

  @FXML
  private void importNodes() {
    csvHandler.importNodes(nodesFilename.getText());
  }

  @FXML
  private void exportNodes() {
    csvHandler.exportNodes(nodesFilename.getText());
  }

  @FXML
  private void importEdges() {
    csvHandler.importEdges(edgesFilename.getText());
  }

  @FXML
  private void exportEdges() {
    csvHandler.exportEdges(edgesFilename.getText());
  }

  @FXML
  private void importEmployees() {
    csvHandler.importEmployees(employeesFilename.getText());
  }

  @FXML
  private void exportEmployees() {
    csvHandler.exportEmployees(employeesFilename.getText());
  }

  @FXML
  private void importRequests() {
    csvHandler.importServiceRequests(requestsFilename.getText());
  }

  @FXML
  private void exportRequests() {
    csvHandler.exportServiceRequests(requestsFilename.getText());
  }
}
