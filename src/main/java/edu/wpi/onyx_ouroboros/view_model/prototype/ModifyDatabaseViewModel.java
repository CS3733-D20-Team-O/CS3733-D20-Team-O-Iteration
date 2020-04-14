package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.lang.CharSequence;

public class ModifyDatabaseViewModel extends ViewModelBase {

  @FXML private Button addNodeBtn;
  @FXML private TextField addIDField;
  @FXML private TextField addXCoordField;
  @FXML private TextField addYCoordField;
  @FXML private TextField addFloorField;
  @FXML private TextField addBuildingField;
  @FXML private TextField addNodeTypeField;
  @FXML private TextField addLongNameField;
  @FXML private TextField addShortNameField;

  @FXML private Button updateNodeBtn;
  @FXML private TextField updateNodeIDField;
  @FXML private TextField updateNodeNodeField;

  @FXML private Button deleteNodeBtn;
  @FXML private TextField deleteNodeIDField;

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  // Do whatever you need with connecting UI and this class (do not modify other classes) to make
  // these methods. This purposefully gives you a lot of flexibility to design the UI as you want.
  // Thus, feel free to change the method names and the parameters they take below, but make sure
  // this class provides the appropriate functionality (add, update, & delete)

  // change println to log.(other stuff)
  // bind button press to fns

  @FXML
  public void addNode(PrototypeNode node) {
    if (addIDField.equals("")) {
      System.out.println("An error occurred when attempting to add a node.\nThe field must not be empty!");
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

      CharSequence buildingCS = null;
      CharSequence nodeTypeCS = null;
      CharSequence longNameCS = null;
      CharSequence shortNameCS = null;
      String building = buildingCS.toString();
      String nodeType = nodeTypeCS.toString();
      String longName = longNameCS.toString();
      String shortName = shortNameCS.toString();

      // fill in the data from text fields
      PrototypeNode aNode = new PrototypeNode(nodeID, x, y, floor, building, nodeType, longName, shortName);

      // add node to database
      database.addNode(aNode);
    }
  }

  @FXML
  public void updateNode(String nodeID, PrototypeNode node) {
    if (updateNodeIDField.equals("") || updateNodeNodeField.equals("")) {
      System.out.println("An error occurred when attempting to update a node.");
      if (updateNodeIDField.equals("")) {
        System.out.println("The ID field must not be empty!");
      }
      if (updateNodeNodeField.equals("")) {
        System.out.println("The node field must not be empty!");
      }
    } else {
      // update node in database
      database.updateNode(nodeID, node);
    }
  }

  @FXML
  public void deleteNode(String nodeID) {
    if (deleteNodeIDField.equals("")) {
      System.out.println("An error occurred when attempting to delete a node.\nThe field must not be empty!");
    } else {
      // delete node from database
      database.deleteNode(nodeID);
    }
  }
}
