package edu.wpi.cs3733.d20.teamO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import edu.wpi.cs3733.d20.teamO.view_model.NavigationBar;
import java.io.IOException;
import java.util.Stack;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Navigator {

  private final Injector injector;

  private final Stack<Node> stack = new Stack<>();

  @Getter
  private final StackPane root = new StackPane();

  @Setter
  private String mainFXML;

  public IdleDetector idleDetector;

        /*
    todo incorporate this animation code too
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(imgView2.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t->{
            // remove pane and restore scene 1
            root1.getChildren().setAll(rectangle1);
        });
     */

  protected void start() {
    idleDetector = new IdleDetector(root, this);
  }

  public void push(String title, String fxmlLocation) throws IOException {
    // Get a copy of the current page
    val currentRoot = stack.peek();

    // Create and set up the new page
    val newRoot = createNewPage(title, fxmlLocation);
    root.getChildren().add(newRoot);

    // Execute the transition
    val width = root.getWidth();
    val start = new KeyFrame(Duration.ZERO,
        new KeyValue(currentRoot.translateXProperty(), 0),
        new KeyValue(newRoot.translateXProperty(), width));
    val end = new KeyFrame(Duration.seconds(0.15),
        new KeyValue(currentRoot.translateXProperty(), -width),
        new KeyValue(newRoot.translateXProperty(), 0));
    val slide = new Timeline(start, end);
    slide.setOnFinished(e -> {
      root.getChildren().remove(currentRoot);
      stack.push(newRoot);
    });
    slide.play();
  }

  public void pop() throws IOException {
    // If the stack is already empty, initialize with a main page
    if (stack.size() < 1) {
      empty();
    }
    // If the stack size has only one element in it, don't pop
    if (stack.size() <= 1) {
      return;
    }

    // Get a copy of the current root
    val currentRoot = stack.pop();

    // Get the new root via the stack and add it back to the root for the slide transition
    val newRoot = stack.peek();
    root.getChildren().add(newRoot);

    // Execute the transition
    val width = root.getWidth();
    val start = new KeyFrame(Duration.ZERO,
        new KeyValue(newRoot.translateXProperty(), -width),
        new KeyValue(currentRoot.translateXProperty(), 0));
    val end = new KeyFrame(Duration.seconds(0.15),
        new KeyValue(newRoot.translateXProperty(), 0),
        new KeyValue(currentRoot.translateXProperty(), width));
    val slide = new Timeline(start, end);
    slide.setOnFinished(e -> root.getChildren().remove(currentRoot));
    slide.play();
  }

  public void empty() throws IOException {
    stack.empty();
    root.getChildren().clear();
    root.getChildren().add(getLoader().load(getClass().getResourceAsStream(mainFXML)));
    stack.push(root.getChildren().get(0));
  }

  private Node createNewPage(String title, String fxmlLocation) throws IOException {
    // Create the new root for the page
    val newRoot = new AnchorPane();

    // Set up the navigation bar for the new page
    val navBarLoader = getLoader();
    val navBarRoot = (Region) navBarLoader.load(getClass()
        .getResourceAsStream("views/NavigationBar.fxml"));
    ((NavigationBar) navBarLoader.getController()).setTitle(title);
    AnchorPane.setTopAnchor(navBarRoot, 0.0);
    AnchorPane.setLeftAnchor(navBarRoot, 0.0);
    AnchorPane.setRightAnchor(navBarRoot, 0.0);
    newRoot.getChildren().add(navBarRoot);

    // Set up the actual body (content) of the new page
    val contentRoot = (Region) getLoader().load(getClass().getResourceAsStream(fxmlLocation));
    // The height is not available right away, so set top bounds when it is available
    navBarRoot.heightProperty().addListener(((observable, oldValue, newValue) ->
        AnchorPane.setTopAnchor(contentRoot, newValue.doubleValue())));
    AnchorPane.setLeftAnchor(contentRoot, 0.0);
    AnchorPane.setRightAnchor(contentRoot, 0.0);
    AnchorPane.setBottomAnchor(contentRoot, 0.0);
    newRoot.getChildren().add(contentRoot);
    return newRoot;
  }

  private FXMLLoader getLoader() {
    return injector.getInstance(FXMLLoader.class);
  }
}
