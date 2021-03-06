package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d20.teamO.model.data.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ImportExportCSVViewModel extends Dialog.DialogViewModel {

  private final CSVHandler csvHandler;
  private final SnackBar snackBar;
  private final FileChooser fileChooser;

  private final SimpleStringProperty fileLocation = new SimpleStringProperty("");

  private List<DataType> dataTypes;
  private DataType currentDataType;

  @FXML
  private JFXComboBox<String> typeSelector;
  @FXML
  private JFXButton importButton, exportButton;

  @Override
  protected void start(URL location, ResourceBundle resources) {
    // Set the data types
    dataTypes = new ArrayList<>(Arrays.asList(
        new DataType("Nodes", csvHandler::importNodes, csvHandler::exportNodes),
        new DataType("Edges", csvHandler::importEdges, csvHandler::exportEdges),
        new DataType("Employees", csvHandler::importEmployees, csvHandler::exportEmployees)));
    // Add all the data types to the selection box
    dataTypes.forEach(dataType -> typeSelector.getItems().add(dataType.name));
    // Add a listener to the selection box
    typeSelector.getSelectionModel().selectedIndexProperty().addListener(
        (observable, oldNum, newNum) -> currentDataType = dataTypes.get(newNum.intValue()));
    // Pre-select the first option
    typeSelector.getSelectionModel().select(0);
  }

  @FXML
  private void selectFile() {
    fileChooser.setTitle("Open CSV Data File");
    val csvExtensionFilter = new ExtensionFilter("CSV Files", "*.csv");
    fileChooser.getExtensionFilters().add(csvExtensionFilter);
    val selectedFile = fileChooser.showOpenDialog(typeSelector.getScene().getWindow());
    if (selectedFile != null) {
      importButton.setDisable(false);
      exportButton.setDisable(false);
      setFileLocation(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  private void importData() {
    if (currentDataType.importData.apply(getFileLocation())) {
      snackBar.show("Successfully imported the " + currentDataType.name.toLowerCase());
    } else {
      snackBar.show("Failed to import the " + currentDataType.name.toLowerCase());
    }
  }

  @FXML
  private void exportData() {
    if (currentDataType.exportData.apply(getFileLocation())) {
      snackBar.show("Successfully exported the " + currentDataType.name.toLowerCase());
    } else {
      snackBar.show("Failed to export the " + currentDataType.name.toLowerCase());
    }
  }

  public String getFileLocation() {
    return fileLocation.get();
  }

  @SuppressWarnings("unused") // Called by JavaFX
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
