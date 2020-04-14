package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ModifyDatabaseViewModel extends ViewModelBase {

  Logger logger = Logger.getLogger(ModifyDatabaseViewModel.class.getName());
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
  private TextField updateNodeIDField;
  @FXML
  private TextField updateNodeNodeField;
  @FXML
  private Button deleteNodeBtn;
  @FXML
  private TextField deleteNodeIDField;

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  // Do whatever you need with connecting UI and this class (do not modify other classes) to make
  // these methods. This purposefully gives you a lot of flexibility to design the UI as you want.
  // Thus, feel free to change the method names and the parameters they take below, but make sure
  // this class provides the appropriate functionality (add, update, & delete)

  // change println to log.(other stuff)
  // bind button press to fns

  @FXML
  public void addNodeBtn(ActionEvent e) {
    if (e.getSource() == addNodeBtn) {
      logger.setLevel(Level.SEVERE);
      if (addIDField.getText().equals("")) {
        logger.severe(
            "An error occurred when attempting to add a node.\nThe field must not be empty!");
      } else {
        // haha data parsing go brrrrr
        CharSequence idCS = addIDField.getCharacters();
        String nodeID = idCS.toString();

        CharSequence xCS, yCS, floorCS;
        int x, y, floor;
        xCS = addXCoordField.getCharacters();
        x = Integer.parseInt(xCS, 0, xCS.length(), 10);
        yCS = addYCoordField.getCharacters();
        y = Integer.parseInt(yCS, 0, yCS.length(), 10);
        floorCS = addFloorField.getCharacters();
        floor = Integer.parseInt(floorCS, 0, floorCS.length(), 10);

        String building = addBuildingField.getCharacters().toString();
        String nodeType = addNodeTypeField.getCharacters().toString();
        String longName = addLongNameField.getCharacters().toString();
        String shortName = addShortNameField.getCharacters().toString();

        // fill in the data from text fields
        PrototypeNode aNode = new PrototypeNode(nodeID, x, y, floor, building, nodeType, longName,
            shortName);
        addNode(aNode);
      }
    }
  }

  @FXML
  public void addNode(PrototypeNode aNode) {
    // add node to database
    database.addNode(aNode);
  }

  @FXML
  public void updateNodeBtn(ActionEvent e) {
    if (e.getSource() == updateNodeBtn) {
      logger.setLevel(Level.SEVERE);
      if (updateNodeIDField.getText().equals("") || updateNodeNodeField.getText().equals("")) {
        logger.severe("An error occurred when attempting to update a node.");
        if (updateNodeIDField.getText().equals("")) {
          logger.severe("The ID field must not be empty!");
        }
        if (updateNodeNodeField.getText().equals("")) {
          logger.severe("The node field must not be empty!");
        }
      } else {
        // update node in database
        String nodeID = updateNodeIDField.getCharacters().toString();
        PrototypeNode node = new PrototypeNode(nodeID,
            24, 25, 6,
            "Sample Building",
            "Node Type",
            "Long Name",
            "Short Name");
        updateNode(nodeID, node);
      }
    }
  }

  @FXML
  public void updateNode(String nodeID, PrototypeNode node) {
    database.updateNode(nodeID, node);
  }

  @FXML
  public void deleteNodeBtn(ActionEvent e) {
    if (e.getSource() == deleteNodeBtn) {
      logger.setLevel(Level.SEVERE);
      if (deleteNodeIDField.getText().equals("")) {
        logger.severe(
            "An error occurred when attempting to delete a node.\nThe field must not be empty!");
      } else {
        // delete node from database
        String nodeID = deleteNodeIDField.getCharacters().toString();
        deleteNode(nodeID);
      }
    }
  }

  @FXML
  public void deleteNode(String nodeID) {
    database.deleteNode(nodeID);
  }
}
