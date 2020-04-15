package edu.wpi.onyx_ouroboros.view_model.prototype;

import edu.wpi.onyx_ouroboros.model.DependencyInjector;
import edu.wpi.onyx_ouroboros.model.data.PrototypeNode;
import edu.wpi.onyx_ouroboros.model.data.database.DatabaseWrapper;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeDeletedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeInsertedEvent;
import edu.wpi.onyx_ouroboros.model.data.database.events.DatabaseNodeUpdatedEvent;
import edu.wpi.onyx_ouroboros.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;
import org.greenrobot.eventbus.Subscribe;

public class DisplayDatabaseViewModel extends ViewModelBase {

  @FXML
  private TableView<PrototypeNode> nodeDisplayTable;
  @FXML
  private TableColumn<PrototypeNode, String> nodeID;
  @FXML
  private TableColumn<PrototypeNode, Integer> xCoord;
  @FXML
  private TableColumn<PrototypeNode, Integer> yCoord;
  @FXML
  private TableColumn<PrototypeNode, Integer> floor;
  @FXML
  private TableColumn<PrototypeNode, String> building;
  @FXML
  private TableColumn<PrototypeNode, String> nodeType;
  @FXML
  private TableColumn<PrototypeNode, String> longName;
  @FXML
  private TableColumn<PrototypeNode, String> shortName;
  @FXML
  private ObservableList displayTableItems;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources); // do not remove

    nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xCoord.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    yCoord.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));

    deleteAll();
    val currentNodes = DependencyInjector.create(DatabaseWrapper.class).export();
    addAll(currentNodes);
  }

  private void deleteAll() {
    nodeDisplayTable.getItems().clear();
  }

  private void addAll(List<PrototypeNode> nodes) {
    ObservableList<PrototypeNode> displayTableItems = FXCollections.observableArrayList();
    displayTableItems.addAll(nodes);
    nodeDisplayTable.setItems(displayTableItems);
  }

  @Subscribe
  public void onNodeDeleted(DatabaseNodeDeletedEvent event) {
    val nodeToDelete = event.getNodeID();

    for (val item : nodeDisplayTable.getItems()) {

      if (item.getNodeID().equals(nodeToDelete)) {
        nodeDisplayTable.getItems().remove(item);
        break;
      }
    }//end of tableLength for loop
  }

  @Subscribe
  public void onNodeInsert(DatabaseNodeInsertedEvent event) {
    // event.getNode() will return the new node's properties
    // todo finish the onNodeInsert event
  }

  @Subscribe
  public void onNodeUpdated(DatabaseNodeUpdatedEvent event) {
    // event.getNodeID() will return the ID of the old node (so you know which one to update
    // event.getNode() contains all the new properties
    // todo finish the onNodeDeleted event
  }
}
