package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ModifyDatabaseViewModel extends ViewModelBase {

  @FXML private Button addNodeBtn;
  @FXML private Button updateNodeBtn;
  @FXML private Button deleteNodeBtn;
  @FXML private TextField addNodeField;
  @FXML private TextField updateNodeFieldID;
  @FXML private TextField updateNodeFieldNode;
  @FXML private TextField deleteNodeField;

  private final DatabaseWrapper database = DependencyInjector.create(DatabaseWrapper.class);

  // Do whatever you need with connecting UI and this class (do not modify other classes) to make
  // these methods. This purposefully gives you a lot of flexibility to design the UI as you want.
  // Thus, feel free to change the method names and the parameters they take below, but make sure
  // this class provides the appropriate functionality (add, update, & delete)

  @FXML
  public void addNode(PrototypeNode node) {
    if (addNodeField.equals("")) {
      System.out.println("An error occurred when attempting to add a node.\nThe field must not be empty!");
    } else {
      database.addNode(node);
    }
  }

  @FXML
  public void updateNode(String nodeID, PrototypeNode node) {
    if (updateNodeFieldID.equals("") || updateNodeFieldNode.equals("")) {
      System.out.println("An error occurred when attempting to update a node.");
      if (updateNodeFieldID.equals("")) {
        System.out.println("The ID field must not be empty!");
      }
      if (updateNodeFieldNode.equals("")) {
        System.out.println("The node field must not be empty!");
      }
    } else {
      // update node in database
      database.updateNode(nodeID, node);
    }
  }

  @FXML
  public void deleteNode(String nodeID) {
    if (deleteNodeField.equals("")) {
      System.out.println("An error occurred when attempting to delete a node.\nThe field must not be empty!");
    } else {
      // delete node from database
      database.deleteNode(nodeID);
    }
  }
}
