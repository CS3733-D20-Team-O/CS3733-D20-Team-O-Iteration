package edu.wpi.cs3733.d20.teamO.model.network;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.scene.image.Image;
import lombok.Value;
import lombok.val;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class NetworkUtilities {

  private static final String apiURLBase =
      "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=";
  private static final String key = "AIzaSyAQnd9KK2Mw3uxzspVQmkmr93njvjDk0cM";
  private static final String longDynamicLinkPrefix = "https://hospitalkiosk.page.link?link=";

  /**
   * Creates a JavaFX Image of a QRCode that points to the provided link
   *
   * @param url the link the QRCode should point to
   * @return an Image of a QRCode that links to the web app with the supplied steps
   */
  public static Image createQRCode(String url) {
    val stream = QRCode.from(url).to(ImageType.PNG).withSize(400, 400).stream();
    return new Image(new ByteArrayInputStream(stream.toByteArray()));
  }

  public static String shortenURL(String longURL) throws IOException {
    // Setup the connection
    val url = new URL(apiURLBase + key);
    val con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    // Setup the data we need to send
    val encodedURL = URLEncoder.encode(longURL, StandardCharsets.UTF_8);
    val request = new FirebaseShortLinkRequest(longDynamicLinkPrefix + encodedURL);
    val requestJson = new Gson().toJson(request);

    // Write to the api
    try (val os = con.getOutputStream()) {
      val input = requestJson.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    // Process the returned data
    try (val reader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)) {
      val response = new Gson().fromJson(reader, FirebaseShortLinkResponse.class);
      return response.shortLink;
    }
  }

  @Value
  private static class FirebaseShortLinkRequest {

    String longDynamicLink;
  }

  @Value
  private static class FirebaseShortLinkResponse {

    String shortLink;
  }
}
