package edu.wpi.cs3733.d20.teamO.model.network;

import java.util.Map;
import lombok.Value;

@Value
public class CuttlyResponse {

  Map<String, Object> url;

  /*
  Response options:
    1: the shortened link comes from the domain that shortens the link
    2: the entered link is not a link.
    3: the preferred link name is already taken
    4: Invalid API key
    5: the link has not passed the validation. Includes invalid characters
    6: The link provided is from a blocked domain
    7: OK - the link has been shortened
   */

  /**
   * Gets the status of this response as per the Cuttly API
   *
   * @return the status as returned by the API
   */
  public int getStatus() {
    return (int) url.get("status");
  }

  /**
   * @return whether this response succeeded
   */
  public boolean isValid() {
    return getStatus() == 7;
  }

  /**
   * @return the short link or null if the request failed
   */
  public String getShortLink() {
    return (String) url.get("shortLink");
  }
}
