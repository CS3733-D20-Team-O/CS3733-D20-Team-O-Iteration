package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/**
 * Tests the CSV import & export wizard
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class ImportExportCSVViewModelTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  CSVHandler csvHandler;
  @Mock
  SnackBar snackBar;
  @Mock
  FileChooser fileChooser;
  @Mock
  File testFile;

  @InjectMocks
  ImportExportCSVViewModel viewModel;

  // Sets up the stage for testing
  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/ImportExportCSV.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testComboBox() {
    clickOn("Nodes");
    Arrays.asList("Nodes", "Edges", "Employees")
        .forEach(str -> verifyThat(str, javafx.scene.Node::isVisible));
  }

  @Test
  public void testFilePicking() {
    when(fileChooser.getExtensionFilters()).thenReturn(FXCollections.observableArrayList());
    when(testFile.getAbsolutePath()).thenReturn("Test Passed");
    when(fileChooser.showOpenDialog(any()))
        .thenReturn(null)
        .thenReturn(testFile);
    clickOn("Select a file");
    assertEquals("", viewModel.getFileLocation());
    clickOn("Select a file");
    assertEquals("Test Passed", viewModel.getFileLocation());
    verifyThat("Test Passed", javafx.scene.Node::isVisible);
  }

  @Test
  public void testImportButton() {
    when(fileChooser.getExtensionFilters()).thenReturn(FXCollections.observableArrayList());
    when(testFile.getAbsolutePath()).thenReturn("Test Passed");
    when(fileChooser.showOpenDialog(any())).thenReturn(testFile);
    when(csvHandler.importNodes("Test Passed")).thenReturn(true).thenReturn(false);
    clickOn("Select a file");
    // Test import success
    clickOn("Import");
    verify(snackBar, times(1)).show("Successfully imported the nodes");
    // Test import fail
    clickOn("Import");
    verify(snackBar, times(1)).show("Failed to import the nodes");
  }

  @Test
  public void testExportButton() {
    when(fileChooser.getExtensionFilters()).thenReturn(FXCollections.observableArrayList());
    when(testFile.getAbsolutePath()).thenReturn("Test Passed");
    when(fileChooser.showOpenDialog(any())).thenReturn(testFile);
    when(csvHandler.exportNodes("Test Passed")).thenReturn(true).thenReturn(false);
    clickOn("Select a file");
    // Test import success
    clickOn("Export");
    verify(snackBar, times(1)).show("Successfully exported the nodes");
    // Test import fail
    clickOn("Export");
    verify(snackBar, times(1)).show("Failed to export the nodes");
  }
}
