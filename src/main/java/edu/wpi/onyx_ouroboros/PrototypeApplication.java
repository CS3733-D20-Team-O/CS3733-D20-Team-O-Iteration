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



  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    // todo load all 4 windows from views/prototype/*.fxml

    val dataDisplayRoot = "views/prototype/DisplayDatabase.fxml";
    val dataDisplayTitle = "Database Display";
    val dataModifyRoot = "views/prototype/ModifyDatabase.fxml";
    val dataModifyTitle = "Database Modify";
    val dataDownloadRoot = "views/prototype/DownloadDatabase.fxml";
    val dataDownloadTitle = "Database Download";
    val astarDemoRoot = "views/prototype/AStar.fxml";
    val astarDemoTitle = "A* Demo Display";

    openWindow(dataDisplayRoot, dataDisplayTitle);
    openWindow(dataModifyRoot, dataModifyTitle);
    openWindow(dataDownloadRoot, dataDownloadTitle);
    openWindow(astarDemoRoot, astarDemoTitle);

  }

  private void openWindow(String fxmlLocation, String stageTitle) throws IOException {
    val displayRoot = (Parent) FXMLLoader.load(getClass().getResource(fxmlLocation));
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
