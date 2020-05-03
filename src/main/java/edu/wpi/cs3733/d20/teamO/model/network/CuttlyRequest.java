package edu.wpi.cs3733.d20.teamO.model.network;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.val;

public class CuttlyRequest {

  private static final String key = "106633b34d19392de98b7d741058c433950e0";
  private static final String requestURL = "https://cutt.ly/api/api.php?key=KEY&short=LINK";

  private final URL urlToLoad;

  /**
   * Constructs a url shorten-er request object with the given link to shorten
   *
   * @param linkToShorten the link to shorten
   * @throws MalformedURLException in case the provided link to shorten is not a valid URL
   */
  public CuttlyRequest(String linkToShorten) throws MalformedURLException {
    val encodedLink = URLEncoder.encode(linkToShorten, StandardCharsets.UTF_8);
    val apiRequestURL = requestURL.replace("KEY", key).replace("LINK", encodedLink);
    urlToLoad = new URL(apiRequestURL);
  }

  /**
   * Makes the shorten request and returns the response object
   *
   * @return the response to the request
   * @throws IOException in case the network stream cannot be read
   */
  public CuttlyResponse makeRequest() throws IOException {
    val stream = new InputStreamReader(urlToLoad.openStream());
    return new Gson().fromJson(stream, CuttlyResponse.class);
  }
}
