package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ImportExportCSVViewModel extends ViewModelBase {

  private final CSVHandler csvHandler;

  private final SimpleStringProperty fileLocation = new SimpleStringProperty("");

  private List<DataType> dataTypes;
  private DataType currentDataType;

  @FXML
  private StackPane root;
  @FXML
  private VBox container;
  @FXML
  private JFXComboBox<String> typeSelector;
  @FXML
  private JFXButton importButton, exportButton;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Add elevation to container
    JFXDepthManager.setDepth(container, 3);

    // Set the data types
    dataTypes = new ArrayList<>(Arrays.asList(
        new DataType("Nodes", csvHandler::importNodes, csvHandler::exportNodes),
        new DataType("Edges", csvHandler::importEdges, csvHandler::exportEdges),
        new DataType("Employees", csvHandler::importEmployees, csvHandler::exportEmployees),
        new DataType("Service Requests",
            csvHandler::importServiceRequests, csvHandler::exportServiceRequests)));
    // Add all the data types to the selection box
    dataTypes.forEach(dataType -> typeSelector.getItems().add(dataType.name));
    // Add a listener to the selection box
    typeSelector.getSelectionModel().selectedIndexProperty().addListener(
        (observable, oldNum, newNum) -> currentDataType = dataTypes.get(newNum.intValue()));
    // Pre-select the first option
    typeSelector.getSelectionModel().select(0);
  }

  private void showSnackBar(String text) {
    JFXSnackbar bar = new JFXSnackbar(root);
    val label = new Label(text);
    label.setStyle("-fx-text-fill: floralwhite");
    val container = new HBox(label);
    container.setStyle("-fx-background-color: #323232;  -fx-background-insets: 16");
    container.setPadding(new Insets(32)); // total padding, including margin
    bar.enqueue(new SnackbarEvent(container));
  }

  private void showConfirmation(boolean didWork) {
    showSnackBar(didWork ? "Operation completed successfully" : "Operation failed. Invalid file?");
  }

  @FXML
  private void selectFile() {
    val fileChooser = new FileChooser();
    fileChooser.setTitle("Open CSV Data File");
    fileChooser.getExtensionFilters()
        .addAll(new ExtensionFilter("CSV Files", "*.csv"));
    val selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
    if (selectedFile != null) {
      importButton.setDisable(false);
      exportButton.setDisable(false);
      setFileLocation(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  private void importData() {
    showConfirmation(currentDataType.importData.apply(getFileLocation()));
  }

  @FXML
  private void exportData() {
    showConfirmation(currentDataType.exportData.apply(getFileLocation()));
  }

  public String getFileLocation() {
    return fileLocation.get();
  }

  public SimpleStringProperty fileLocationProperty() {
    return fileLocation;
  }

  public void setFileLocation(String fileLocation) {
    this.fileLocation.set(fileLocation);
  }

  /**
   * Represents a data type that can be imported/exported from the database
   * <p>
   * (Like nodes, edges, employees, and service requests)
   */
  @Value
  private static class DataType {

    String name;
    Function<String, Boolean> importData, exportData;
  }
}
