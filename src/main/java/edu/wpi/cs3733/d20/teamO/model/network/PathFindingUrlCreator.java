package edu.wpi.cs3733.d20.teamO.model.network;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javafx.scene.image.Image;
import lombok.Value;
import lombok.val;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

/**
 * Handles interoperability with the web app
 */
public class PathFindingUrlCreator {

  /**
   * The base url to the web application
   */
  private static final String baseUrl =
      "https://cs3733-d20-team-o.github.io/CS3733-D20-Team-O-Iteration?";

  /**
   * Creates a url for the path finding web application based on the supplied list of steps
   *
   * @param steps the steps to create the web app url from
   * @return a url to the customized version of the web app
   */
  public static String createURL(List<Step> steps) {
    val url = new StringBuilder(baseUrl);
    for (int i = 0; i < steps.size(); ++i) {
      url.append(steps.get(i).getUrlParameters(i + 1));
    }
    url.append("s=").append(steps.size());
    return url.toString();
  }

  /**
   * Creates a JavaFX Image of a QRCode that points to the web app with the supplied steps
   * @param steps the steps to fuel the web app
   * @return an Image of a QRCode that links to the web app with the supplied steps
   */
  public static Image createQRCode(List<Step> steps) {
    val stream = QRCode.from(createURL(steps))
        .to(ImageType.PNG).withSize(400, 400).stream();
    return new Image(new ByteArrayInputStream(stream.toByteArray()));
  }

  /**
   * Represents a step in the path finding process, including the floor, textual instructions, and a
   * list of nodes in the path.
   */
  @Value
  public static class Step {

    String instructions;
    List<Node> nodes;
    int floor;

    /**
     * Gets url parameters for this step given a step number via the following key:
     * <ul>
     * <li>s - number of steps</li>
     * <li>[step]i - instructions for [step]</li>
     * <li>[step]f - floor number of [step]</li>
     * <li>[step]n - number of nodes in [step]</li>
     * <li>[step]x[i] - [step]'s i-th node's x coordinate</li>
     * <li>[step]y[i] - [step]'s i-th node's y coordinate</li>
     * </ul>
     *
     * @param step the number of this step (1, 2, 3, ...) (counting starts at 1!)
     * @return the url parameters of this step as a string
     */
    private String getUrlParameters(int step) {
      val url = new StringBuilder();
      url.append(step).append("f=").append(floor).append('&');
      url.append(step).append("n=").append(nodes.size()).append('&');
      val encodedInstructions = URLEncoder.encode(instructions, StandardCharsets.UTF_8);
      url.append(step).append("i=").append(encodedInstructions).append('&');
      for (int i = 0; i < nodes.size(); ++i) {
        val node = nodes.get(i);
        url.append(step).append('x').append(i).append('=').append(node.getXCoord()).append('&');
        url.append(step).append('y').append(i).append('=').append(node.getYCoord()).append('&');
      }
      return url.toString();
    }
  }
}
