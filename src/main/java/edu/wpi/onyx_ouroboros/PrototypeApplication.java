package edu.wpi.onyx_ouroboros;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;


@Slf4j
public class PrototypeApplication extends Application {

  private String resourceLoc = "resources/edu/wpi/onyx_ouroboros/views/prototype/";

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    // todo load all 4 windows from views/prototype/*.fxml

    String dataDisplayRoot = "views/prototype/DisplayDatabase.fxml";
    String dataDisplayTitle = "Database Display";
    String dataModifyRoot = "views/prototype/ModifyDatabase.fxml";
    String dataModifyTitle = "Database Modify";
    String dataDownloadRoot = "views/prototype/DownloadDatabase.fxml";
    String dataDownloadTitle = "Database Download";
    String astarDemoRoot = "views/prototype/AStar.fxml";
    String astarDemoTitle = "A* Demo Display";

    openWindow(dataDisplayRoot, dataDisplayTitle);
    openWindow(dataModifyRoot, dataModifyTitle);
    openWindow(dataDownloadRoot, dataDownloadTitle);
    openWindow(astarDemoRoot, astarDemoTitle);

  }

  private void openWindow(String rootLoc, String stageTitle) throws IOException {
    Parent displayRoot = FXMLLoader.load(getClass().getResource(rootLoc));
    val displayStage = new Stage();
    val displayScene = new Scene(displayRoot);
    displayStage.setScene(displayScene);
    displayStage.setTitle(stageTitle);
    displayStage.show();

  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
