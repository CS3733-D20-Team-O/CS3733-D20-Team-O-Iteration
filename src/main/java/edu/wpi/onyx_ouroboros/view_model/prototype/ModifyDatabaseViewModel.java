package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModifyDatabaseViewModel extends ViewModelBase {

  @FXML
  private Button addNodeBtn;
  @FXML
  private TextField addIDField;
  @FXML
  private TextField addXCoordField;
  @FXML
  private TextField addYCoordField;
  @FXML
  private TextField addFloorField;
  @FXML
  private TextField addBuildingField;
  @FXML
  private TextField addNodeTypeField;
  @FXML
  private TextField addLongNameField;
  @FXML
  private TextField addShortNameField;
  @FXML
  private Button updateNodeBtn;
  @FXML
  private TextField updateOldNodeIDField;
  @FXML
  private TextField updateNewNodeIDField;
  @FXML
  private TextField updateXCoordField;
  @FXML
  private TextField updateYCoordField;
  @FXML
  private TextField updateFloorField;
  @FXML
  private TextField updateBuildingField;
  @FXML
  private TextField updateNodeTypeField;
  @FXML
  private TextField updateLongNameField;
  @FXML
  private TextField updateShortNameField;
  @FXML
  private Button deleteNodeBtn;
  @FXML
  private TextField deleteNodeIDField;

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  // Do whatever you need with connecting UI and this class (do not modify other classes) to make
  // these methods. This purposefully gives you a lot of flexibility to design the UI as you want.
  // Thus, feel free to change the method names and the parameters they take below, but make sure
  // this class provides the appropriate functionality (add, update, & delete)

  @FXML
  public void addNodeClicked(ActionEvent e) {
    if (e.getSource() == addNodeBtn) {
      if (addIDField.getText().equals("")) {
        // log not recognized
        log.error("An error occurred when attempting to add a node.\nThe field must not be empty!");
      } else {
        // haha data parsing go brrrrr
        String nodeID = addIDField.getText();

        String xS = addXCoordField.getText();
        String yS = addYCoordField.getText();
        String floorS = addFloorField.getText();
        int x = Integer.parseInt(xS);
        int y = Integer.parseInt(yS);
        int floor = Integer.parseInt(floorS);

        String building = addBuildingField.getText();
        String nodeType = addNodeTypeField.getText();
        String longName = addLongNameField.getText();
        String shortName = addShortNameField.getText();

        // fill in the data from text fields
        PrototypeNode aNode = new PrototypeNode(nodeID, x, y, floor, building, nodeType, longName,
            shortName);
        database.addNode(aNode);
      }
    }
  }

  @FXML
  public void updateNodeClicked(ActionEvent e) {
    String oldNodeID = "";
    String newNodeID = "";
    String xS = "";
    String yS = "";
    String floorS;
    String building = "";
    String nodeType = "";
    String longName = "";
    String shortName = "";
    int x = 9001;
    int y = 9001;
    int floor = 2;
    if (e.getSource() == updateNodeBtn) {
      if (updateOldNodeIDField.getText().equals("")) {
        log.error("An error occurred when attempting to update a node.");
        log.error("The old ID field must not be empty!");
      } else if (updateNewNodeIDField.getText().equals("")) {
        log.error("An error occurred when attempting to update a node.");
        log.error("The new ID field must not be empty!");
      } else {
        // update node in database
        if (!(updateOldNodeIDField.getText().isBlank())) {
          oldNodeID = updateOldNodeIDField.getText();
        }

        if (!(updateNewNodeIDField.getText().isBlank())) {
          newNodeID = updateNewNodeIDField.getText();
        }

        if (!(updateXCoordField.getText().isBlank())) {
          xS = updateXCoordField.getText();
          x = Integer.parseInt(xS);
        }

        if (!(updateYCoordField.getText().isBlank())) {
          yS = updateYCoordField.getText();
          y = Integer.parseInt(yS);
        }

        if (!(updateFloorField.getText().isBlank())) {
          floorS = updateFloorField.getText();
          floor = Integer.parseInt(floorS);
        }

        if (!(updateBuildingField.getText().isBlank())) {
          building = updateBuildingField.getText();
        }

        if (!(updateNodeTypeField.getText().isBlank())) {
          nodeType = updateNodeTypeField.getText();
        }

        if (!(updateLongNameField.getText().isBlank())) {
          longName = updateLongNameField.getText();
        }

        if (!(updateShortNameField.getText().isBlank())) {
          shortName = updateShortNameField.getText();
        }

        // fill in the data from text fields
        PrototypeNode newNode = new PrototypeNode(newNodeID, x, y, floor, building, nodeType,
            longName, shortName);
        database.updateNode(oldNodeID, newNode);
      }
    }
  }

  @FXML
  public void deleteNodeClicked(ActionEvent e) {
    if (e.getSource() == deleteNodeBtn) {
      if (deleteNodeIDField.getText().equals("")) {
        log.error(
            "An error occurred when attempting to delete a node.\nThe field must not be empty!");
      } else {
        // delete node from database
        String nodeID = deleteNodeIDField.getText();
        database.deleteNode(nodeID);
      }
    }
  }
}
