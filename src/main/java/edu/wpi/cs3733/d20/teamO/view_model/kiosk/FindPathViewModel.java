package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.effects.JFXDepthManager;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.path_finding.SelectedPathFinder;
import edu.wpi.cs3733.d20.teamO.view_model.NodeMapView;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FindPathViewModel extends ViewModelBase {

  private enum State {
    START,
    END,
    DRAWN,
    COMBO_START,
    COMBO_STOP
  }

  private State currentState = State.START;

  @FXML
  private BorderPane root;
  @FXML
  private Pane clipper;
  @FXML
  private NodeMapView nodeMapViewController;
  @FXML
  private AnchorPane sideBar;
  @FXML
  private Label floorLabel;
  @FXML
  private JFXSlider zoomSlider;

  @FXML
  private JFXComboBox<String> startRoom, stopRoom;

  @FXML
  private JFXComboBox<String> startFloor, stopFloor;

  private Map<String, Node> nodeMap;
  private final DatabaseWrapper database;
  private Node beginning, finish, defaultStart, defaultStop;

  private final SelectedPathFinder pathFinder;


  /**
   * Called when a ViewModel's views have been completely processed and can be used freely
   *
   * @param location  the location used to resolve relative paths for the root object, or null
   * @param resources the resources used to localize the root object, or null
   */
  @Override
  protected void start(URL location, ResourceBundle resources) {
    val clipRect = new Rectangle();
    clipRect.widthProperty().bind(clipper.widthProperty());
    clipRect.heightProperty().bind(clipper.heightProperty());
    clipper.setClip(clipRect);

    JFXDepthManager.setDepth(sideBar, 2);
    nodeMap = database.exportNodes();
    nodeMapViewController.setNodeMap(nodeMap);
    defaultStart = nodeMap.get("AEXIT00101");
    defaultStop = nodeMap.get("AEXIT00101");
    //TODO fix start and stop to not die when node is deleted
    nodeMapViewController.setOnNodeLeftTapListener(node -> {
      switch (currentState) {
        case START:
          beginning = node;
          startFloor.getSelectionModel().select(Integer.valueOf(node.getFloor()));
          startRoom.getSelectionModel().select(node.getLongName());
          currentState = State.DRAWN;
          break;
        case END:
          finish = node;
          stopFloor.getSelectionModel().select(Integer.valueOf(node.getFloor()));
          stopRoom.getSelectionModel().select(node.getLongName());
          drawPath();
          currentState = State.DRAWN;
          break;
        default:
          break;
      }
    });
    nodeMapViewController.setOnMissRightTapListener((x, y) -> {
    });//TODO remove

    //TODO abstract into helper method
    // Populate the floors combobox with available nodes
    nodeMap.values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(startFloor.getItems()::add);
    // Set up the populating of locations on each floor
    startFloor.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      startRoom.getItems().clear();
      nodeMap.values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(startRoom.getItems()::add);
    });

    // Populate the floors combobox with available nodes
    nodeMap.values().stream()
        .map(Node::getFloor).distinct().sorted()
        .forEachOrdered(stopFloor.getItems()::add);
    // Set up the populating of locations on each floor
    stopFloor.getSelectionModel().selectedItemProperty().addListener((o, oldFloor, newFloor) -> {
      stopRoom.getItems().clear();
      nodeMap.values().stream()
          .filter(node -> newFloor.equals(node.getFloor()))
          .map(Node::getLongName).sorted()
          .forEachOrdered(stopRoom.getItems()::add);
    });

    resetPath();
  }

  /**
   * resets the path
   */
  @FXML
  void resetPath() {
    beginning = defaultStart;
    finish = defaultStop;
    startFloor.getSelectionModel().select(Integer.valueOf(defaultStart.getFloor()));
    startRoom.getSelectionModel().select(defaultStart.getLongName());
    stopFloor.getSelectionModel().select(Integer.valueOf(defaultStop.getFloor()));
    stopRoom.getSelectionModel().select(defaultStop.getLongName());
    nodeMapViewController.draw();
  }


  //TODO translate floor
  @FXML
  public void floorDownPressed(ActionEvent actionEvent) {
    nodeMapViewController.decrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawPath();
  }

  @FXML
  public void floorUpPressed(ActionEvent actionEvent) {
    nodeMapViewController.incrementFloor();
    nodeMapViewController.draw();
    floorLabel.setText("Floor " + nodeMapViewController.getFloor());
    drawPath();
  }

  @FXML
  public void setZoom() {
    //nodeMapViewController.zoom(zoomSlider.getValue() / 100);
  }

  @FXML
  public void zoomOutPressed() {
    zoomSlider.decrement();
    setZoom();
  }

  @FXML
  public void zoomInPressed() {
    zoomSlider.increment();
    setZoom();
  }

  private void drawPath() {
    List<Node> nodes = pathFinder.getCurrentPathFinder().findPathBetween(beginning, finish);
    for (int i = 0; i < nodes.size() - 1; i++) {
      nodeMapViewController.drawEdge(nodes.get(i), nodes.get(i + 1));
    }
  }

  /**
   * sets beginning on combo box change
   */
  @FXML
  private void onStartCombo() {
    currentState = State.COMBO_START;
    comboState();
  }

  /**
   * sets finish on combo box change
   */
  @FXML
  private void onStopCombo() {
    currentState = State.COMBO_STOP;
    comboState();
  }


  //TODO get rid of click buttons
  @FXML
  private void setStart() {
    currentState = State.START;
  }

  @FXML
  private void setEnd() {
    currentState = State.END;
  }

  /**
   * sets beginning or finish based on combo box input
   */
  private void comboState() {
    switch (currentState) {
      case COMBO_START:
        for (Node n : nodeMap.values()) {
          if (n.getLongName().equals(startRoom.getSelectionModel().getSelectedItem())) {
            beginning = n;
            break;
          }
        }
        nodeMapViewController.draw();
        drawPath();
        break;
      case COMBO_STOP:
        for (Node n : nodeMap.values()) {
          if (n.getLongName().equals(stopRoom.getSelectionModel().getSelectedItem())) {
            finish = n;
            break;
          }
        }
        nodeMapViewController.draw();
        drawPath();
        break;
      default:
        break;
    }
  }
}
