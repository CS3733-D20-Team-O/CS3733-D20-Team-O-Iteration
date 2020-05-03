package edu.wpi.cs3733.d20.teamO.model.network;

import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import lombok.val;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class NetworkUtilities {

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
}
