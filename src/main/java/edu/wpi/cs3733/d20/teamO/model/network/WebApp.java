package edu.wpi.cs3733.d20.teamO.model.network;

import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

/**
 * Handles interoperability with the web app
 */
public class WebApp {

  /**
   * The base url to the web application
   */
  private static final String baseUrl =
      "https://cs3733-d20-team-o.github.io/CS3733-D20-Team-O-Iteration?";

  /**
   * Creates a url for the path finding web application based on the supplied list of steps
   *
   * @param color the color as an integer (can be null) to use behind the maps
   * @param steps the steps to create the web app url from
   * @return a url to the customized version of the web app
   */
  public static String createURL(Integer color, List<Step> steps) {
    val url = new StringBuilder(baseUrl);
    for (int i = 0; i < steps.size(); ++i) {
      url.append(steps.get(i).getUrlParameters(i + 1));
    }
    url.append("s=").append(steps.size());
    if (color != null) {
      url.append("&c=").append(color);
    }
    return url.toString();
  }

  /**
   * Creates a qr code from the given steps
   *
   * @param color the color as an integer (can be null) to use behind the maps
   * @param steps the steps to use to create the qr code
   * @return the qr code image or null if one was could not be created
   * @throws IOException in case of network issues
   */
  public static Image createQRCode(Integer color, List<Step> steps) throws IOException {
    return NetworkUtilities.createQRCode(NetworkUtilities.shortenURL(createURL(color, steps)));
  }

/*
Parameter key:
 c - color of background as int (can be null)
 s - number of steps
 [step]i - number of instructions in [step]
 [step]d[i] - description of the instruction at index i
 [step]i[i] - icon for the instruction at index i [hl, l, sl, s, sr, r, hr, d, n]
 [step]f - floor string of [step] with building identifier before floor
 [step]n - number of nodes in [step]
 [step]x[i] - [step]'s i-th node's x coordinate
 [step]y[i] - [step]'s i-th node's y coordinate
 */

  /**
   * Represents a step in the path finding process, including the floor, textual instructions, and a
   * list of nodes in the path.
   */
  @Value
  public static class Step {

    List<Instruction> instructions;
    List<Node> nodes;
    Building building;
    String floor;

    /**
     * Gets url parameters for this step given a step number via the established web app api
     *
     * @param step the number of this step (1, 2, 3, ...) (counting starts at 1!)
     * @return the url parameters of this step as a string
     */
    private String getUrlParameters(int step) {
      val url = new StringBuilder();

      // Add floor information
      url.append(step).append("f=").append(building.buildingIdentifier).append(floor).append('&');

      // Add node information
      url.append(step).append("n=").append(nodes.size()).append('&');
      for (int i = 0; i < nodes.size(); ++i) {
        val node = nodes.get(i);
        url.append(step).append('x').append(i).append('=').append(node.getXCoord()).append('&');
        url.append(step).append('y').append(i).append('=').append(node.getYCoord()).append('&');
      }

      // Add instruction information
      url.append(step).append("i=").append(instructions.size()).append('&');
      for (int i = 0; i < instructions.size(); ++i) {
        val instruction = instructions.get(i);
        url.append(step).append('i').append(i).append('=')
            .append(instruction.icon.iconIdentifier).append('&');
        val encodedDescription = URLEncoder.encode(instruction.directions, StandardCharsets.UTF_8);
        url.append(step).append('d').append(i).append('=').append(encodedDescription).append('&');
      }

      // Return the resulting string
      return url.toString();
    }

    @Value
    public static class Instruction {

      String directions;
      Icon icon;

      @RequiredArgsConstructor
      public enum Icon {
        HARD_LEFT("hl"),
        LEFT("l"),
        SLIGHT_LEFT("sl"),
        STRAIGHT("s"),
        SLIGHT_RIGHT("sr"),
        RIGHT("r"),
        HARD_RIGHT("hr"),
        DESTINATION("d"),
        NAVIGATION("n");

        private final String iconIdentifier;
      }
    }

    @RequiredArgsConstructor
    public enum Building {
      MAIN_CAMPUS("m"), FAULKNER("f");

      private final String buildingIdentifier;

      /**
       * @param buildingName the name of the building
       * @return the building from the given building name
       */
      public static Building from(String buildingName) {
        switch (buildingName.toLowerCase()) {
          case "maincampus":
          case "main campus":
            return Building.MAIN_CAMPUS;
          case "faulkner":
          default:
            return Building.FAULKNER;
        }
      }
    }
  }
}
